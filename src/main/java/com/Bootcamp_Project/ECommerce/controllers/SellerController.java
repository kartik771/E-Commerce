package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.dto.AddressDTO;
import com.Bootcamp_Project.ECommerce.dto.SellerRegisterDTO;
import com.Bootcamp_Project.ECommerce.dto.UpdatePasswordDTO;
import com.Bootcamp_Project.ECommerce.dto.UpdateSellerProfileDTO;
import com.Bootcamp_Project.ECommerce.exception.UserExistsException;
import com.Bootcamp_Project.ECommerce.repos.SellerRepository;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerService sellerService;
    @Autowired
    private TokenRepository tokenRepository;
    @PostMapping("register")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody SellerRegisterDTO sellerRegisterDTO) throws UserExistsException {


        return new ResponseEntity<>(sellerService.createSeller(sellerRegisterDTO) , HttpStatus.OK);
    }

    @GetMapping("view/profile")
    public ResponseEntity<?> showSellerProfile()
    {
        return new ResponseEntity<>(sellerService.showProfile() , HttpStatus.OK);

    }

    @PatchMapping("change/password")
    public ResponseEntity<?> changePassword( @RequestBody UpdatePasswordDTO updatePasswordDTO)
    {

            return new ResponseEntity<>(sellerService.updatePassword( updatePasswordDTO), HttpStatus.OK);

    }
    @PatchMapping("update/profile")
    public ResponseEntity<?> updateProfile( @RequestBody UpdateSellerProfileDTO updateSellerProfileDTO)
    {

            return new ResponseEntity<>(sellerService.updateProfile( updateSellerProfileDTO), HttpStatus.OK);


    }

    @PatchMapping("update/address/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id  ,@RequestBody AddressDTO addressDTO)
    {

            return new ResponseEntity<>(sellerService.updateSellerAddress(id , addressDTO) , HttpStatus.OK);


    }
}
