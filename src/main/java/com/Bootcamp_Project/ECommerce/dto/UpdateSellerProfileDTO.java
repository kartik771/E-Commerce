package com.Bootcamp_Project.ECommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSellerProfileDTO {

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String companyName;
    private String companyContact;
    private String gst;

}
