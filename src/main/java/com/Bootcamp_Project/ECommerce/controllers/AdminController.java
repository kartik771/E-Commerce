package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.service.AdminService;
import com.Bootcamp_Project.ECommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("show-customer")
    public ResponseEntity<?> showCustomer(
            @RequestParam(required = false , defaultValue = "0") Integer pageOffSet,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize ,
            @RequestParam(required = false , defaultValue = "id") String sortBy)
    {
        return new ResponseEntity<>(adminService.showCustomers(pageOffSet , pageSize , sortBy) , HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("show-seller")
    public ResponseEntity<?> showSeller(  @RequestParam(required = false , defaultValue = "0") Integer pageOffSet,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize ,
                                          @RequestParam(required = false , defaultValue = "id") String sortBy)
    {
        return new ResponseEntity<>(adminService.showSellers(pageOffSet , pageSize , sortBy) , HttpStatus.OK);
    }

    @PutMapping("activate-deactivate/user/{id}")
    public  ResponseEntity<?> activateUser(@PathVariable Long id)
    {
        return new ResponseEntity<>(adminService.activateDeactivateUser(id) , HttpStatus.OK);
    }


    @PutMapping("lock-unlock/user/{id}")
    public ResponseEntity<?> lockUser(@PathVariable Long id)
    {
        return new ResponseEntity<>(adminService.lockUnlockUser(id) , HttpStatus.OK);
    }



}
