package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.*;
import com.Bootcamp_Project.ECommerce.entities.category.Category;
import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataField;
import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataFieldValues;
import com.Bootcamp_Project.ECommerce.entities.product.Product;
import com.Bootcamp_Project.ECommerce.entities.product.ProductVariation;
import com.Bootcamp_Project.ECommerce.entities.user.Seller;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.exception.UnSolvedException;
import com.Bootcamp_Project.ECommerce.exception.UserExistsException;
import com.Bootcamp_Project.ECommerce.exception.UserNotFoundException;
import com.Bootcamp_Project.ECommerce.repos.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;
    public MessageDTO addProduct (ProductDTO productDTO ) throws UserExistsException, UnSolvedException {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Category> allByLeafNodes = categoryRepository.findByIdLeaf();
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Category is not" +
                " found or does not exist."));
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Seller is not" +
                " found or does not exist."));
        if(allByLeafNodes.contains(category))
        {
            List<Product> products = productRepository.findAllByNameIgnoreCase(productDTO.getName());
            for(Product product : products)
            {
                if(Objects.equals(product.getBrand() , productDTO.getBrand())
                && Objects.equals(product.getCategory() , category)
                && Objects.equals(product.getSeller() , seller))
                {
                    throw new UserExistsException("The seller already exists!");
                }
            }
            Product product = new Product();
            product.setBrand(productDTO.getBrand());
            product.setName(productDTO.getName());
            if(productDTO.getDescription()!=null)
            {
                product.setDescription(productDTO.getDescription());
            }
            product.setCreatedBy(seller.getFirstName());
            product.setSeller(seller);
            product.setCategory(category);
            product.setCancellable(productDTO.getIsCancellable());
            product.setReturnable(productDTO.getIsReturnable());
            productRepository.save(product);
            String body = "Product Name " + productDTO.getName() + " Has Been added successfully.";
            emailSenderService.sendSimpleEmail(email , body , "PRODUCT ADDED");
            messageDTO.setMessage("The Product has been added");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }

        throw new UnSolvedException("Some Error occurred");
    }

    public ProductDTO showProduct(Long id)
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ProductDTO productDTO = new ProductDTO();
        System.out.println(email);
        Product product = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("The credentials entered are invalid! "));
        if(userRepository.existsByEmail(email) && !product.isDeleted()) {
            productDTO.setBrand(product.getBrand());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategoryId(product.getCategory().getId());
            productDTO.setCategoryName(product.getCategory().getName());
            productDTO.setIsReturnable(product.isReturnable());
            productDTO.setIsCancellable(product.isCancellable());
            productDTO.setName(product.getName());
            return productDTO;
        }
        throw new EntityNotFoundException("The product is either deleted or does not exist");

    }

    public MessageDTO updateProduct(Long id , ProductDTO productDTO) throws UserExistsException, UnSolvedException {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product is not found or does not exist."));
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Seller is not found or does not exist."));
        List<Product> products = productRepository.findAllBySellerId(seller.getId());
            for(Product product1 : products)
            {
                if(Objects.equals(product1.getBrand() , productDTO.getBrand())
                        && Objects.equals(product1.getCategory() , product.getCategory())
                        && Objects.equals(product1.getSeller() , seller))
                {
                    throw new UserExistsException("The product already exists!");
                }
            }
            if(!Objects.isNull(productDTO.getBrand()))
            {
                product.setBrand(productDTO.getBrand());
            }
            if(!Objects.isNull(productDTO.getName()))
            {
                product.setName(productDTO.getName());
            }
            if(!Objects.isNull(productDTO.getDescription()))
            {
                product.setDescription(productDTO.getDescription());
            }
            if(!Objects.isNull(productDTO.getIsCancellable()))
            {
                product.setCancellable(productDTO.getIsCancellable());
            }
            if(!Objects.isNull(productDTO.getIsReturnable()))
            {
                product.setReturnable(productDTO.getIsReturnable());
            }
            if(!Objects.isNull(productDTO.getCategoryId()))
            {
                product.getCategory().setId(product.getId());
            }
            if(!Objects.isNull(productDTO.getCategoryName()))
            {
                product.getCategory().setName(product.getName());
            }
            product.setModifiedBy(seller.getFirstName());

            productRepository.save(product);
            messageDTO.setMessage("The Product has been updated!");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
    }

    public MessageDTO deleteProduct(Long id) throws UnSolvedException {
        MessageDTO messageDTO = new MessageDTO();
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException ("The details entered are not valid or do not exist!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The user does not exists"));
        if(userRepository.existsByEmail(email))
        {
            product.setModifiedBy(seller.getFirstName());
            product.setDeleted(true);
            productRepository.save(product);
            messageDTO.setMessage("The Product has been deleted successfully");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }
        throw new UnSolvedException("The invalid credentials have been found!");
    }

    public MessageDTO addProductVariation(ProductVariationDTO productVariationDTO) throws UnSolvedException {
        MessageDTO messageDTO = new MessageDTO();
        Product product = productRepository.findById(productVariationDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException ("The details entered are not valid or do not exist!"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException ("The details entered are not valid or do not exist!"));
        if(productVariationDTO.getQuantity() <0 || productVariationDTO.getPrice()<0)
        {
            throw new UnSolvedException("Please Enter valid Quantity and Price.");
        }
        if(!product.isActive() && product.isDeleted())
        {
            throw new UnSolvedException("Product is either not activated or deleted.");
        }
        Map<String , String> setMetaData = productVariationDTO.getMetaData();
        Map<String ,  String> metaData = new HashMap<>();
        for(Map.Entry<String , String> data : setMetaData.entrySet())
        {
            if(!categoryMetadataFieldRepository.existsByNameIgnoreCase(data.getKey()))
            {
                throw new UnSolvedException("The given field does not exist1.");
            }
            CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findByNameIgnoreCase(data.getKey());
            if(!categoryMetadataFieldValuesRepository.existsByCategoryIdAndCategoryMetadataFieldId(product.getCategory().getId(),categoryMetadataField.getId()))
            {
                throw new UnSolvedException("The given field does not exist2.");

            }
            CategoryMetadataFieldValues categoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findByCategoryIdAndCategoryMetadataFieldId(product.getCategory().getId(), categoryMetadataField.getId());
            String[] str = categoryMetadataFieldValues.getFieldValues().split(",");
            for(String values : str)
            {
                if(values.equalsIgnoreCase(data.getValue()))
                {
                    metaData.put(data.getKey(), values);
                    break;
                }
            }
        }
        ProductVariation productVariation = new ProductVariation();
        productVariation.setCreatedBy(seller.getFirstName());
        productVariation.setProduct(product);
        productVariation.setPrice(productVariationDTO.getPrice());
        productVariation.setMetadata(metaData);
        productVariation.setQuantityAvailable(productVariationDTO.getQuantity());
        productVariation.setSellerId(seller.getId());
        productVariationRepository.save(productVariation);
        messageDTO.setMessage("The Product variation has been added successfully");
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public ProductVariationDTO viewProductVariation(Long id)
    {
        ProductVariation productVariation = productVariationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No valid product variation found to corresponding id"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No valid Seller"));
        if(productVariation.getSellerId() == seller.getId())
        {
            if(!productVariation.getProduct().isDeleted())
            {
                ProductVariationDTO productVariationDTO = new ProductVariationDTO();
                productVariationDTO.setProductVariationId(id);
                productVariationDTO.setPrice(productVariation.getPrice());
                productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
                productVariationDTO.setMetaData(productVariation.getMetadata());
//                productVariationDTO.setPrimaryImageName(productVariation.getPrimaryImageName());
//                productVariationDTO.setPrimaryImagePath(productVariation.getPrimaryImageName());

                Product product = productVariation.getProduct();
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                productResponseDTO.setProductId(product.getId());
                productResponseDTO.setName(product.getName());
                productResponseDTO.setDescription(product.getDescription());
                productResponseDTO.setBrand(product.getBrand());
                productResponseDTO.setIsActive(product.isActive());
                productResponseDTO.setIsDeleted(product.isDeleted());
                productVariationDTO.setProduct(productResponseDTO);

                return productVariationDTO;
            }
            throw new EntityNotFoundException("This product is already deleted");
        }
        throw new EntityNotFoundException("Currently logged in user is not the creator of the product");
    }

    public List<ProductVariationViewDTO> showAllProductVariations(Long id)
    {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Does not exist."));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The Seller does not exist."));
        if(product.getSeller().getId() == seller.getId())
        {
            if(!product.isDeleted())
            {
                List<ProductVariation> productVariationList = productVariationRepository.findByProduct(product);
                List<ProductVariationViewDTO> productVariationViewDTOList = new ArrayList<>();
                for(ProductVariation productVariation : productVariationList)
                {
                    ProductVariationViewDTO productVariationViewDTO = new ProductVariationViewDTO();
                    productVariationViewDTO.setProductVariationId(productVariation.getId());
                    productVariationViewDTO.setProductName(product.getName());
                    productVariationViewDTO.setPrice(productVariation.getPrice().longValue());
                    productVariationViewDTO.setIsActive(productVariation.getIsActive());
                    productVariationViewDTO.setMetadata(productVariation.getMetadata());
                    productVariationViewDTO.setQuantityAvailable(productVariation.getQuantityAvailable().longValue());
//                    productVariationViewDTO.setPrimaryImageName(productVariation.getPrimaryImageName().toString());
                    productVariationViewDTOList.add(productVariationViewDTO);
                }
                return productVariationViewDTOList;
            }
            throw new EntityNotFoundException("The product is Already Deleted!");
        }
        throw new EntityNotFoundException("Currently Logged in user is not the creator of the product");
    }

    public List<ProductResponseDTO> showAllProducts()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No valid Seller"));
        List<Product> productList = productRepository.findBySeller(seller);
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for(Product product: productList)
        {
            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
            productResponseDTO.setProductId(product.getId());
            productResponseDTO.setName(product.getName());
            productResponseDTO.setBrand(product.getBrand());
            productResponseDTO.setDescription(product.getDescription());
            productResponseDTO.setIsActive(product.isActive());
            productResponseDTO.setIsDeleted(product.isDeleted());

            CategoryViewDTO categoryViewDTO = new CategoryViewDTO();
            categoryViewDTO.setCategoryId(product.getCategory().getId());
            categoryViewDTO.setCategoryName(product.getCategory().getName());
            categoryViewDTO.setParentCategory(product.getCategory().getParentCategory());
//            categoryViewDTO.setCategoryMetadataFieldValuesList(product.getCategory().getCategoryMetadataFieldValues());
            productResponseDTO.setCategory(categoryViewDTO);

            productResponseDTOList.add(productResponseDTO);
        }
        return productResponseDTOList;
    }

    public MessageDTO updateProductVariation(Long productId , ProductVariationUpdateDTO productUpdateDto)  {
        MessageDTO messageDTO = new MessageDTO();
        Product product = productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("Product does not exist."));
        if(product.isDeleted()){
            throw new EntityNotFoundException("Product is deleted");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("Seller does not exist."));
        if(product.getSeller().getId().equals(seller.getId())) {
            ProductVariation productVariation = new ProductVariation();
            if(!Objects.isNull(productUpdateDto.getQuantity())){
                productVariation.setQuantityAvailable(productUpdateDto.getQuantity());
            }
            if(!Objects.isNull(productUpdateDto.getPrice())){
                productVariation.setPrice(productUpdateDto.getPrice());
            }
            if(!Objects.isNull(productUpdateDto.getIsActive())){
                productVariation.setIsActive(productUpdateDto.getIsActive());
            }
            if(!Objects.isNull(productUpdateDto.getMetaData())){
                productVariation.setMetadata(productUpdateDto.getMetaData());
            }
//            if(productUpdateDto.getPrimaryImageName()!=null){
//                productVariation.setPrimaryImageName(productUpdateDto.getPrimaryImageName());
//            }
            productVariation.setModifiedBy(seller.getFirstName());
            productVariationRepository.save(productVariation);
            messageDTO.setMessage("Product Variation is updated successfully");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }
        throw new EntityNotFoundException("Product does not exists!");
    }

    public ProductViewDTO viewProductCustomer(Long id)
    {
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No valid product Exists"));
        List<ProductVariation> productVariations = productVariationRepository.findByProduct(product);
        if(!product.isDeleted() && product.isActive())
        {
            ProductViewDTO productViewDTO = new ProductViewDTO();
            productViewDTO.setProductId(product.getId());
            productViewDTO.setProductName(product.getName());
            productViewDTO.setDescription(product.getDescription());
            productViewDTO.setBrand(product.getBrand());
            List<ProductVariationViewDTO> productVariationDTOList = new ArrayList<>();
            for(ProductVariation productVariation : productVariations)
            {
                ProductVariationViewDTO productVariationViewDTO = new ProductVariationViewDTO();
                productVariationViewDTO.setProductVariationId(productVariation.getId());
                productVariationViewDTO.setMetadata(productVariation.getMetadata());
                productVariationViewDTO.setQuantityAvailable(productVariation.getQuantityAvailable().longValue());
                productVariationViewDTO.setPrice(productVariation.getPrice().longValue());
//                productVariationViewDTO.setPrimaryImageName(productVariation.getPrimaryImageName().toString());
                productVariationDTOList.add(productVariationViewDTO);
            }
            productViewDTO.setVariations(productVariationDTOList);
            productViewDTO.setDeleted(product.isDeleted());
            productViewDTO.setActive(product.isActive());
            return productViewDTO;
        }
        throw new EntityNotFoundException("No valid product variation found");
    }
    public List<ProductViewDTO> viewAllProducts(Long categoryId)
    {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category Does not exist"));
        if(!categoryRepository.existsByParentCategory(category))
        {
            List<Product> productList = productRepository.findByCategory(category);
            List<ProductViewDTO> productViewDTOList = new ArrayList<>();
            for(Product product : productList)
            {
                if(product.isActive() && !product.isDeleted()) {
                    ProductViewDTO productViewDTO = new ProductViewDTO();
                    productViewDTO.setProductId(product.getId());
                    productViewDTO.setProductName(product.getName());
                    productViewDTO.setBrand(product.getBrand());
                    productViewDTO.setDescription(product.getDescription());

                    List<ProductVariation> productVariations = productVariationRepository.findByProduct(product);

                    if (productVariations.size() >= 1) {
                        List<ProductVariationViewDTO> productVariationViewDTOList = new ArrayList<>();
                        for (ProductVariation productVariation : productVariations) {
                            ProductVariationViewDTO productVariationViewDTO = new ProductVariationViewDTO();
                            productVariationViewDTO.setProductVariationId(productVariation.getId());
                            productVariationViewDTO.setMetadata(productVariation.getMetadata());
                            productVariationViewDTO.setQuantityAvailable(productVariation.getQuantityAvailable().longValue());
                            productVariationViewDTO.setPrice(productVariation.getPrice().longValue());
//                        productVariationViewDTO.setPrimaryImageName(productVariation.getPrimaryImageName().toString());
                            productVariationViewDTOList.add(productVariationViewDTO);
                        }
                        productViewDTO.setVariations(productVariationViewDTOList);
                    }
                    productViewDTO.setDeleted(product.isDeleted());
                    productViewDTO.setActive(product.isActive());

                    CategoryViewDTO categoryViewDTO = new CategoryViewDTO();
                    categoryViewDTO.setCategoryId(product.getCategory().getId());
                    categoryViewDTO.setCategoryName(product.getCategory().getName());
                    categoryViewDTO.setParentCategory(product.getCategory().getParentCategory());
//                categoryViewDTO.setCategoryMetadataFieldValuesList(product.getCategory().getCategoryMetadataFieldValues());
                    productViewDTO.setCategory(categoryViewDTO);

                    productViewDTOList.add(productViewDTO);
                }

            }
            return productViewDTOList;
        }
        throw new EntityNotFoundException("Category is not a leaf category");
    }

    public List<Product> similarProducts(long id){
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No product value exists"));
        if(Objects.isNull(product) || !product.isActive()){
            throw new EntityNotFoundException("No valid product exists!");
        }
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(()-> new EntityNotFoundException("No valid category value exists"));
        List<Product> products = productRepository.findAllByCategory(category);
        return products;
    }

    public MessageDTO activateDeactivateProduct(Long id)
    {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User adminUser = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The user does not exist"));
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException ("The details entered are not valid or do not exist!"));
        String sellerEmail = product.getSeller().getEmail();
        if(product == null)
        {
            throw new UserNotFoundException("No valid Product Found");
        }

        if(!product.isActive())
        {
            product.setModifiedBy(adminUser.getFirstName());
            product.setActive(true);
            productRepository.save(product);
            String body = "Your Product has been activated";
            emailSenderService.sendSimpleEmail(sellerEmail , body , "PRODUCT ACTIVATION MAIL");
            messageDTO.setMessage("The product has been Activated Successfully" );
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }

        product.setActive(false);
        productRepository.save(product);
        String body = "Your Product has been deactivated";
        emailSenderService.sendSimpleEmail(sellerEmail , body , "PRODUCT DEACTIVATION MAIL");

        messageDTO.setMessage("The product has been deactivated Successfully" );
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public ProductViewDTO viewProduct(Long id)
    {
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("No valid product Exists"));
        List<ProductVariation> productVariations = productVariationRepository.findByProduct(product);
        if(!product.isDeleted())
        {
            ProductViewDTO productViewDTO = new ProductViewDTO();
            productViewDTO.setProductId(product.getId());
            productViewDTO.setProductName(product.getName());
            productViewDTO.setDescription(product.getDescription());
            productViewDTO.setBrand(product.getBrand());
            List<ProductVariationViewDTO> productVariationDTOList = new ArrayList<>();
            for(ProductVariation productVariation : productVariations)
            {
                ProductVariationViewDTO productVariationViewDTO = new ProductVariationViewDTO();
                productVariationViewDTO.setProductVariationId(productVariation.getId());
                productVariationViewDTO.setMetadata(productVariation.getMetadata());
                productVariationViewDTO.setQuantityAvailable(productVariation.getQuantityAvailable().longValue());
                productVariationViewDTO.setPrice(productVariation.getPrice().longValue());
//                productVariationViewDTO.setPrimaryImageName(productVariation.getPrimaryImageName().toString());
                productVariationDTOList.add(productVariationViewDTO);
            }
            productViewDTO.setVariations(productVariationDTOList);
            productViewDTO.setDeleted(product.isDeleted());
            productViewDTO.setActive(product.isActive());
            return productViewDTO;
        }
        throw new EntityNotFoundException("No valid product variation found");
    }

    public List<ProductViewDTO> viewAllProducts(int pageOffSet , int pageSize , String sortBy , String order)
    {
        PageRequest page = PageRequest.of(pageOffSet , pageSize , Sort.Direction.valueOf(order), sortBy);
        Page<Product> productList = productRepository.findAll(page);
        List<ProductViewDTO> productViewDTOList = new ArrayList<>();
        for(Product product : productList)
        {
            ProductViewDTO productViewDTO = new ProductViewDTO();
            productViewDTO.setProductId(product.getId());
            productViewDTO.setProductName(product.getName());
            productViewDTO.setDescription(product.getDescription());
            productViewDTO.setBrand(product.getBrand());

            List<ProductVariation> productVariations = productVariationRepository.findByProduct(product);
            List<ProductVariationViewDTO> productVariationViewDTOList = new ArrayList<>();
            for(ProductVariation productVariation : productVariations)
            {
                ProductVariationViewDTO productVariationViewDTO = new ProductVariationViewDTO();
                productVariationViewDTO.setProductVariationId(productVariation.getId());
                productVariationViewDTO.setMetadata(productVariation.getMetadata());
                productVariationViewDTO.setQuantityAvailable(productVariation.getQuantityAvailable().longValue());
                productVariationViewDTO.setPrice(productVariation.getPrice().longValue());
//                productVariationViewDTO.setPrimaryImageName(productVariation.getPrimaryImageName().toString());
                productVariationViewDTOList.add(productVariationViewDTO);
            }
            productViewDTO.setVariations(productVariationViewDTOList);
            productViewDTO.setDeleted(product.isDeleted());
            productViewDTO.setActive(product.isActive());

            productViewDTOList.add(productViewDTO);
        }
        return productViewDTOList;
    }
}
