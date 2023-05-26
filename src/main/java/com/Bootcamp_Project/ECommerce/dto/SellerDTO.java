package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.entities.user.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;
    private String companyName;
    private String companyContact;
    private String gst;
    private List<Address> companyAddress;
}
