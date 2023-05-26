package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.entities.user.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SellerRegisterDTO {
    @Email(message = "Email is not valid")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String password;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",
            message = "Password must be at least 8 characters long, contain at least one letter, one digit, and one special character (@$!%*#?&)")
    private String confirmPassword;
    @NotBlank(message = "Name should not be blank")
    private String firstName;
    private String middleName;
    private String lastName;
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = "It should be 15 characters long.\n" +
                    "The first 2 characters should be a number.\n" +
                    "The next 10 characters should be the PAN number of the taxpayer.\n" +
                    "The 13th character (entity code) should be a number from 1-9 or an alphabet.\n" +
                    "The 14th character should be Z.\n" +
                    "The 15th character should be an alphabet or a number.")
    private String gst;
    @Pattern(regexp="(^[0-9]{10}$)", message = "Contact Number should be of 10 digits.")
    private String companyContact;
    @NotBlank
    private String companyName;

    private Address companyAddress;
}
