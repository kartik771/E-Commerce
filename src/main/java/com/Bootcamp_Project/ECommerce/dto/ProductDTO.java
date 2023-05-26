package com.Bootcamp_Project.ECommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private String name;
    private String brand;
    private String description;
    private Long categoryId;
    private Boolean isCancellable ;
    private Boolean isReturnable;
    private String categoryName;
}
