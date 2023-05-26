package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.*;
import com.Bootcamp_Project.ECommerce.entities.category.Category;
import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataField;
import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataFieldValues;
import com.Bootcamp_Project.ECommerce.entities.compositeKeys.CategoryMetadataCompositeKey;
import com.Bootcamp_Project.ECommerce.entities.product.Product;
import com.Bootcamp_Project.ECommerce.entities.user.User;
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

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;

    public MessageDTO createMetadataField(String name) throws UserExistsException {
        MessageDTO messageDTO = new MessageDTO();
        if(categoryMetadataFieldRepository.existsByName(name))
        {
            throw new UserExistsException("The MetadataField Already exists");
        }
        CategoryMetadataField categoryMetadataField = new CategoryMetadataField();
        categoryMetadataField.setName(name);
        categoryMetadataFieldRepository.save(categoryMetadataField);
        Long id = categoryMetadataField.getId();
        messageDTO.setMessage("The MetadataField has been created " + id);
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public List<CategoryMetadataFieldDTO> viewMetadataField(int pageOffSet , int pageSize , String order,String sortBy)
    {
        PageRequest page = PageRequest.of(pageOffSet , pageSize , Sort.Direction.valueOf(order) , sortBy);
        Page<CategoryMetadataField> categoryMetadataFields = categoryMetadataFieldRepository.findAll(page);
//        List<CategoryMetadataField> categoryMetadataFields1 = categoryMetadataFieldRepository.findAll();
        List<CategoryMetadataFieldDTO> categoryMetadataFieldDTOList = new ArrayList<>();
        for (CategoryMetadataField categoryMetadataField : categoryMetadataFields)
        {
            CategoryMetadataFieldDTO categoryMetadataFieldDTO = new CategoryMetadataFieldDTO();
            categoryMetadataFieldDTO.setName(categoryMetadataField.getName());
            categoryMetadataFieldDTOList.add(categoryMetadataFieldDTO);
        }
        return categoryMetadataFieldDTOList;
    }

    public MessageDTO addCategory(CategoryDTO categoryDTO) throws UserExistsException {
        MessageDTO messageDTO = new MessageDTO();
        System.out.println(categoryDTO);
        if (categoryRepository.existsByName(categoryDTO.getName()))
        {
            throw new UserExistsException("The Category already exists");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());

        if(!Objects.isNull(categoryDTO.getParentName()))
        {
            Category parentCategory = categoryRepository.findByName(categoryDTO.getParentName()).orElse(null);
            if(Objects.isNull(parentCategory))
            {
                throw new UserNotFoundException("Parent Category does not exists");
            }
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);
        messageDTO.setMessage("Category is Added");
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public CategoryResponseDTO getCategories(Long id)
    {
        if(!categoryRepository.existsById(id))
        {
            throw new EntityNotFoundException("Given parameters are not present.");
        }
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        Category category = categoryRepository.findById(id).orElse(null);
        categoryResponseDTO.setCurrentCategory(category);

        List<Category> parentCategory = new ArrayList<>();
        Category category1 = category.getParentCategory();
        while(category1!=null)
        {
            Category category2 = categoryRepository.findById(category1.getId()).orElse(null);
            parentCategory.add(category2);
            category1 = category2.getParentCategory();
        }
        categoryResponseDTO.setParent(parentCategory);
        List<Category> categoryList = categoryRepository.findByParentCategory(category);
        List<Category> child = new ArrayList<>(categoryList);
        categoryResponseDTO.setChild(child);
        return categoryResponseDTO;
    }
    public List<Category> getAllCategories(int offset , int pageSize , String order , String sort)
    {
        PageRequest page = PageRequest.of(offset, pageSize , Sort.Direction.valueOf(order) , sort);
        Page<Category> categories = categoryRepository.findAll(page);
        List<Category> categoryList = new ArrayList<>();
        for(Category category : categories)
        {
            Category category1 = new Category();
            category1.setName(category.getName());
            category1.setCategoryMetadataFieldValues(category.getCategoryMetadataFieldValues());
            category1.setId(category.getId());
            category1.setProducts(category.getProducts());
            category1.setParentCategory(category.getParentCategory());
            categoryList.add(category1);
        }
        return categoryList;
    }

    public MessageDTO updateCategory(Long id , CategoryUpdateDTO categoryUpdateDTO) throws UserExistsException {
        MessageDTO messageDTO = new MessageDTO();
        if(!categoryRepository.existsById(id))
        {
            throw new EntityNotFoundException("The category doesn't exists!");
        }
        if(categoryRepository.existsByName(categoryUpdateDTO.getName()))
        {
            throw new UserExistsException("The category already exists.");
        }
        Category category = categoryRepository.findById(id).orElse(null);
        category.setName(categoryUpdateDTO.getName());
        categoryRepository.save(category);
        messageDTO.setTimeStamp(LocalDateTime.now());
        messageDTO.setMessage("The Category has been updated successfully");
        return messageDTO;
    }

    public MessageDTO addMetaDataFieldValue(CategoryMetadataFieldValuesDTO categoryMetadataFieldValuesDTO)
    {
        MessageDTO messageDTO = new MessageDTO();
        if(categoryMetadataFieldValuesDTO.getCategoryId() == null)
        {
            throw new EntityNotFoundException("The category does not exist");
        }
        if(categoryMetadataFieldValuesDTO.getMetadataId() == null)
        {
            throw new EntityNotFoundException("The metadata does not exist");
        }
        if(categoryMetadataFieldValuesDTO.getValues()== null)
        {
            throw new EntityNotFoundException("The value is not present.");
        }
        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepository.findById(categoryMetadataFieldValuesDTO.getMetadataId()).orElse(null);
        Category category = categoryRepository.findById(categoryMetadataFieldValuesDTO.getCategoryId()).orElse(null);
        String[] metaDataFieldValue = categoryMetadataFieldValuesDTO.getValues().split(",");
        Set<String> set = new HashSet<>();
        for(String str :metaDataFieldValue )
        {
            set.add(str);
        }

        String metaDataFieldValueAns = String.join("," , set);
        CategoryMetadataCompositeKey categoryMetadataCompositeKey = new CategoryMetadataCompositeKey();
        categoryMetadataCompositeKey.setCategory(categoryMetadataFieldValuesDTO.getCategoryId());
        categoryMetadataCompositeKey.setCategoryMetadataField(categoryMetadataCompositeKey.getCategoryMetadataField());
        CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues.setFieldValues(metaDataFieldValueAns);
        categoryMetadataFieldValues.setCategory(category);
        categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
        categoryMetadataFieldValues.setCompositeKey(categoryMetadataCompositeKey);
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
        messageDTO.setMessage("The Category Metadata field values have been successfully added");
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public MessageDTO updateMetadataFieldValues(CategoryMetadataFieldValuesDTO categoryMetadataFieldValuesDTO)
    {
        MessageDTO messageDTO = new MessageDTO();
        if(categoryMetadataFieldValuesDTO.getCategoryId() == null)
        {
            throw new EntityNotFoundException("The category does not exist");
        }
        if(categoryMetadataFieldValuesDTO.getMetadataId() == null)
        {
            throw new EntityNotFoundException("The metadata does not exist");
        }

        CategoryMetadataCompositeKey metadataCompositeKey=new CategoryMetadataCompositeKey();
        metadataCompositeKey.setCategory(categoryMetadataFieldValuesDTO.getCategoryId());
        metadataCompositeKey.setCategoryMetadataField(categoryMetadataFieldValuesDTO.getMetadataId());
        CategoryMetadataFieldValues categoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findById(metadataCompositeKey).orElseThrow(()-> new EntityNotFoundException("The Category Metadata Field Value does not exist"));
        String[] categoryMetadataFieldValues1 = categoryMetadataFieldValuesDTO.getValues().split(",");
        String[] categoryMetadataFieldValues2 = categoryMetadataFieldValues.getFieldValues().split(",");
        Set<String> set = new HashSet<>(Arrays.asList(categoryMetadataFieldValues1));
        set.addAll(Arrays.asList(categoryMetadataFieldValues2));
        categoryMetadataFieldValues.setFieldValues(String.join("," ,set));
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
        messageDTO.setMessage("The Category Metadata field values have been successfully updated");
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public List<Category> showAllCategories()
    {
        return categoryRepository.findByIdLeaf();
    }

    public List<Category> showAllCustomerCategories(Long id)
    {
        if(id == null)
        {
            throw new EntityNotFoundException("Please Enter valid credentials");
        }
        else
        {
            if(!categoryRepository.existsById(id))
            {
                return null;
            }
            Category category = categoryRepository.findById(id).orElse(null);
            List<Category> categoryList = categoryRepository.findAllByParentCategory(category);
            return categoryList;
        }
    }

    public List<Object> filteredCategory(long id){
        Category category = categoryRepository.findById(id).orElse(null);
        if(Objects.isNull(category)){
            throw new EntityNotFoundException("Category does not exist.");
        }
        List<Object> usefulLeafs = new ArrayList<>();
        if(categoryRepository.existsByParentCategory(category)) {
            CategoryFilterDTO categoryFilterDTO = new CategoryFilterDTO();
            List<Category> leafs = categoryRepository.findByIdLeaf();
            usefulLeafs.add(category);
            Set<String> brands = new HashSet<>();
            for (Category category1 : leafs) {
                Category category2 = category1;
                while (!Objects.isNull(category1.getParentCategory())) {
                    category1 = categoryRepository.findById(category1.getParentCategory().getId()).orElse(null);
                    if (!Objects.isNull(category1) && category1.getId() == id) {
                        List<Product> products = category2.getProducts();
                        for (Product product : products) {
                            brands.add(product.getBrand());
                        }
                    }
                }
            }
            categoryFilterDTO.setBrands(brands);
            usefulLeafs.add(categoryFilterDTO);
            return usefulLeafs;
        }
        List<Product> products = category.getProducts();
        Set<String> brands = new HashSet<>();
        for (Product product : products) {
            brands.add(product.getBrand());
        }
        CategoryFilterDTO categoryFilterDTO = new CategoryFilterDTO();

        categoryFilterDTO.setBrands(brands);
        categoryFilterDTO.setMinimumPrice(productVariationRepository.minimumPrice(id));
        categoryFilterDTO.setMaximumPrice(productVariationRepository.maximumPrice(id));
        usefulLeafs.add(category);
        usefulLeafs.add(categoryFilterDTO);
        return usefulLeafs;
    }
}
