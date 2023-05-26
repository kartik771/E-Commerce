package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.dto.LoginDTO;
import com.Bootcamp_Project.ECommerce.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @PostMapping("login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginDTO loginDTO)
    {
        return new ResponseEntity<>(loginService.customerLogin(loginDTO), HttpStatus.OK);
    }

}

