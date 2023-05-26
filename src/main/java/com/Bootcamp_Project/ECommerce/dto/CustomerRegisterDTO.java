package com.Bootcamp_Project.ECommerce.dto;

import com.Bootcamp_Project.ECommerce.entities.user.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class CustomerRegisterDTO {

    @Email
    @NotBlank(message = "Please enter a valid email.")
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",
            message = "Password must be at least 8 characters long," +
                    " contain at least one upper letter , on lowercase letter, one digit, and one special character (@$!%*#?&)")
    private String password;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$",
            message = "Password must be at least 8 characters long," +
                    " contain at least one upper letter , on lowercase letter, one digit, and one special character (@$!%*#?&)")
    private String confirmPassword;
    @NotBlank(message = "Please enter a valid name.")
    private String firstName;
    private String middleName;
    @NotBlank(message = "Please enet the valid details")
    private String lastName;
    @Pattern(regexp="(^[0-9]{10}$)", message = "Contact Number should be of 10 digits.")
    private String contact;
    private List<Address> addresses;

}
