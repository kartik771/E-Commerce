package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.service.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Convert;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationViewDTO {
    private String productName;
    private Long productVariationId;
    @Convert(converter = HashMapConverter.class)
    private Map<String , String> metadata;
    private Long quantityAvailable;
    private Long price;
    private String primaryImageName;
    private Boolean isActive;


}
