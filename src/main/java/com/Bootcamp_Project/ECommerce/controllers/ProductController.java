package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.dto.ProductDTO;
import com.Bootcamp_Project.ECommerce.dto.ProductVariationDTO;
import com.Bootcamp_Project.ECommerce.dto.ProductVariationUpdateDTO;
import com.Bootcamp_Project.ECommerce.exception.UnSolvedException;
import com.Bootcamp_Project.ECommerce.exception.UserExistsException;
import com.Bootcamp_Project.ECommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("seller/add/product")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) throws UserExistsException, UnSolvedException {
        return new ResponseEntity<>(productService.addProduct(productDTO) , HttpStatus.OK);
    }

    @PostMapping("seller/add/product-variation")
    public ResponseEntity<?> addProductVariation(@RequestBody ProductVariationDTO productVariationDTO) throws UnSolvedException {
        return new ResponseEntity<>(productService.addProductVariation(productVariationDTO) , HttpStatus.OK);
    }

    @GetMapping("seller/show/product/{id}")
    public ResponseEntity<?> showProduct(@PathVariable Long id)
    {
        return new ResponseEntity<>(productService.showProduct(id) , HttpStatus.OK);
    }

    @GetMapping("seller/show/product-variation/{id}")
    public ResponseEntity<?> showProductVariation(@PathVariable Long id)
    {
        return new ResponseEntity<>(productService.viewProductVariation(id) , HttpStatus.OK);
    }

    @GetMapping("seller/show/products")
    public ResponseEntity<?> showAllProducts()
    {
        return new ResponseEntity<>(productService.showAllProducts() , HttpStatus.OK);
    }

    @GetMapping("seller/show/product-variations/{productId}")
    public ResponseEntity<?> showProductVariations(@PathVariable Long productId)
    {
        return new ResponseEntity<>(productService.showAllProductVariations(productId) , HttpStatus.OK);
    }

    @DeleteMapping("seller/delete/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws UnSolvedException {
        return new ResponseEntity<>(productService.deleteProduct(id) , HttpStatus.OK);
    }

    @PatchMapping("seller/update/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id , @RequestBody ProductDTO productDTO) throws UserExistsException, UnSolvedException {
        return new ResponseEntity<>(productService.updateProduct(id , productDTO) , HttpStatus.OK);
    }


    @PatchMapping("seller/update/product-variation/{productId}")
    public ResponseEntity<?> updateProductVariation(@PathVariable Long productId , @RequestBody ProductVariationUpdateDTO productVariationUpdateDTO)  {
        return new ResponseEntity<>(productService.updateProductVariation(productId , productVariationUpdateDTO) , HttpStatus.OK);
    }

    @GetMapping("customer/view/products/{categoryId}")
    public ResponseEntity<?> viewAllProducts(@PathVariable Long categoryId)
    {
        return new ResponseEntity<>(productService.viewAllProducts(categoryId) , HttpStatus.OK);
    }

    @GetMapping("customer/view/product/{productId}")
    public ResponseEntity<?> showCustomerProducts(@PathVariable Long productId )
    {
        return new ResponseEntity<>(productService.viewProductCustomer(productId) , HttpStatus.OK);
    }

    @GetMapping("customer/view/similar-products/{productId}")
    public ResponseEntity<?> viewSimilarProducts(@PathVariable Long productId)
    {
        return new ResponseEntity<>(productService.similarProducts( productId) , HttpStatus.OK);
    }



    @PatchMapping("admin/activate-deactivate/product/{id}")
    public ResponseEntity<?> activateDeactivateProduct(@PathVariable Long id)
    {
        return new ResponseEntity<>(productService.activateDeactivateProduct(id) , HttpStatus.OK);
    }

    @GetMapping("admin/view/product/{id}")
    public ResponseEntity<?> viewProduct(@PathVariable Long id)
    {
        return new ResponseEntity<>(productService.viewProduct(id) , HttpStatus.OK);
    }

    @GetMapping("admin/view/products")
    public ResponseEntity<?> viewProducts(@RequestParam(required = false , defaultValue = "0") int pageOffSet ,
                                          @RequestParam(required = false , defaultValue = "10") int pageSize ,
                                          @RequestParam (required = false , defaultValue = "ASC")String order,
                                          @RequestParam(required = false , defaultValue = "id") String sortBy)
    {
        return new ResponseEntity<>(productService.viewAllProducts(pageOffSet , pageSize , order , sortBy) , HttpStatus.OK);
    }

}
