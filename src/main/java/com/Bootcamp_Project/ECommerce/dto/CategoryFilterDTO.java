package com.Bootcamp_Project.ECommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryFilterDTO {
    private Set<String> brands;
    private Long minimumPrice;
    private Long maximumPrice;
}
