package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.service.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationUpdateDTO {
    private Long productId;
    private Long productVariationId;
    private String productName;
    private String brand;
    private String description;


    @Min(value = 0 , message = "The quantity should be greater than 0")
    private int quantity;

    @Min(value = 0 , message = "Thr prize should be greater than 0")
    private int price;

    private String primaryImageName;
    private String primaryImagePath;

    @Convert(converter = HashMapConverter.class)
    private Map<String , String> metaData;

    Boolean isCancellable;
    Boolean isReturnable;
    Boolean isActive;

    private ProductResponseDTO product;
}
