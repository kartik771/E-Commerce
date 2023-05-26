package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.entities.category.Category;
import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataFieldValues;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryViewDTO {
    private Long categoryId;
    private String categoryName;
    private Category parentCategory;
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList;
 }
