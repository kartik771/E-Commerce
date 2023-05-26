package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.dto.CategoryDTO;
import com.Bootcamp_Project.ECommerce.dto.CategoryMetadataFieldValuesDTO;
import com.Bootcamp_Project.ECommerce.dto.CategoryUpdateDTO;
import com.Bootcamp_Project.ECommerce.exception.UserExistsException;
import com.Bootcamp_Project.ECommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("admin/create/metadata-field")
    public ResponseEntity<?> createMetaDataFieldValue(@RequestParam String name) throws UserExistsException {
        return new ResponseEntity<>(categoryService.createMetadataField(name) , HttpStatus.OK);
    }

    @GetMapping("admin/show/metadata-field")
    public ResponseEntity<?> showMetaDataField(@RequestParam(required = false , defaultValue = "0") int pageOffSet ,
                                               @RequestParam(required = false , defaultValue = "10") int pageSize ,
                                               @RequestParam (required = false , defaultValue = "ASC")String order,
                                               @RequestParam(required = false , defaultValue = "id") String sortBy)
    {
        return new ResponseEntity<>(categoryService.viewMetadataField(pageOffSet , pageSize , order , sortBy) , HttpStatus.OK);
    }

    @PostMapping("admin/add-category")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) throws UserExistsException {
        return new ResponseEntity<>(categoryService.addCategory(categoryDTO) , HttpStatus.OK);
    }

    @GetMapping("admin/show-category/{id}")
    public ResponseEntity<?> showCategory (@PathVariable Long id)
    {
        return new ResponseEntity<>(categoryService.getCategories(id) , HttpStatus.OK);
    }
    @GetMapping("admin/show/categories")
    public ResponseEntity<?> showCategories(@RequestParam(required = false , defaultValue = "0") int pageOffSet ,
                                            @RequestParam(required = false , defaultValue = "10") int pageSize ,
                                            @RequestParam (required = false , defaultValue = "ASC")String order,
                                            @RequestParam(required = false , defaultValue = "id") String sortBy)
    {
        return new ResponseEntity<>(categoryService.getAllCategories(pageOffSet , pageSize , order , sortBy) , HttpStatus.OK);
    }

    @PatchMapping("admin/update/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id , @RequestBody CategoryUpdateDTO categoryUpdateDTO) throws UserExistsException {
        return new ResponseEntity<>(categoryService.updateCategory(id , categoryUpdateDTO) , HttpStatus.OK);
    }

    @PostMapping("admin/add/metadata-field-value")
    public ResponseEntity<?> addMetadataFieldValue(@RequestBody CategoryMetadataFieldValuesDTO categoryMetadataFieldValuesDTO)
    {
        return new ResponseEntity<>(categoryService.addMetaDataFieldValue(categoryMetadataFieldValuesDTO), HttpStatus.OK);
    }

    @PatchMapping("admin/update/metadata-field-value")
    public ResponseEntity<?> updateMetadataFieldValue(@RequestBody CategoryMetadataFieldValuesDTO categoryMetadataFieldValuesDTO)
    {
        return new ResponseEntity<>(categoryService.updateMetadataFieldValues(categoryMetadataFieldValuesDTO), HttpStatus.OK);
    }

    @GetMapping("seller/show/category")
    public ResponseEntity<?> showCategories()
    {
        return new ResponseEntity<>(categoryService.showAllCategories() , HttpStatus.OK);
    }

    @GetMapping("customer/show/customer-category/{id}")
    public ResponseEntity<?> showCustomerCategory(@PathVariable Long id)
    {
        return new ResponseEntity<>(categoryService.showAllCustomerCategories(id) , HttpStatus.OK);
    }

    @GetMapping("customer/show/filtered-category/{categoryId}")
    public ResponseEntity<?> showFilteredCategory(@PathVariable Long categoryId)
    {
        return new ResponseEntity<>(categoryService.filteredCategory(categoryId) , HttpStatus.OK);
    }
}
