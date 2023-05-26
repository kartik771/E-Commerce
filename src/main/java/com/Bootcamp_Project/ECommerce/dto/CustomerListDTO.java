package com.Bootcamp_Project.ECommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerListDTO {
    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;
}
