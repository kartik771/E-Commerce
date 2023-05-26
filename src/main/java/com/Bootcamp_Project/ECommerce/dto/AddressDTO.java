package com.Bootcamp_Project.ECommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;
    private String label;
}
