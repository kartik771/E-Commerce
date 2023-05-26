package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.entities.user.Tokens;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.repos.CustomerRepository;
import com.Bootcamp_Project.ECommerce.repos.RoleRepository;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import com.Bootcamp_Project.ECommerce.service.CustomerService;
import com.Bootcamp_Project.ECommerce.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LogoutController {
    @Autowired
    private LogoutService logoutService;
    
    @PostMapping("logout")
    public ResponseEntity<?> userLogout(@RequestHeader("token") String token)
    {
        return new ResponseEntity<>(logoutService.logoutUser(token), HttpStatus.OK);
    }
}
