package com.Bootcamp_Project.ECommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductViewDTO {
    private Long productId;
    private String productName;
    private String brand;
    private String description;
    private List<ProductVariationViewDTO> variations;
    private Boolean deleted;
    private Boolean active;
    private CategoryViewDTO category;
}
