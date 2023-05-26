package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.entities.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponseDTO {
    private List<Category> parent;
    private Category currentCategory;
    private List<Category> child;
}
