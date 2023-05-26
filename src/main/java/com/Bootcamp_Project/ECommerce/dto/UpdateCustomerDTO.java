package com.Bootcamp_Project.ECommerce.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateCustomerDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String contact;
    private MultipartFile image;

}
