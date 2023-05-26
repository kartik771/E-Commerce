package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.dto.ForgotPasswordDTO;
import com.Bootcamp_Project.ECommerce.repos.CustomerRepository;
import com.Bootcamp_Project.ECommerce.repos.RoleRepository;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import com.Bootcamp_Project.ECommerce.service.CustomerService;
import com.Bootcamp_Project.ECommerce.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {
    @Autowired
    private UserService userService;

    @PostMapping("forgotpassword")
    public ResponseEntity<?> userForgotPassword(@RequestParam String email)
    {
        return userService.sendForgotPasswordLink(email);
    }
    @PutMapping("resetpassword")
    public ResponseEntity<?> resetUserPassword(@RequestParam UUID token , @RequestBody ForgotPasswordDTO forgotPasswordDTO)
    {
        return userService.resetPassword(token , forgotPasswordDTO);
    }
}
