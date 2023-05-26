package com.Bootcamp_Project.ECommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private String brand;
    private String description;
    private Long categoryId;
    private Boolean isCancellable ;
    private Boolean isReturnable;
    private Boolean isActive;
    private Boolean isDeleted;
    private String categoryName;
    private CategoryViewDTO category;
}
