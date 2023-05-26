package com.Bootcamp_Project.ECommerce.controllers;

import com.Bootcamp_Project.ECommerce.dto.AddressDTO;
import com.Bootcamp_Project.ECommerce.dto.CustomerRegisterDTO;
import com.Bootcamp_Project.ECommerce.dto.UpdateCustomerDTO;
import com.Bootcamp_Project.ECommerce.dto.UpdatePasswordDTO;
import com.Bootcamp_Project.ECommerce.repos.CustomerRepository;
import com.Bootcamp_Project.ECommerce.repos.RoleRepository;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import com.Bootcamp_Project.ECommerce.service.CustomerService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
@Data
public class CustomerController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TokenRepository tokenRepository;



    @PostMapping("register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerRegisterDTO
                                                          customerRegisterDTO) throws Exception {
        return new ResponseEntity<>(customerService.createCustomer(customerRegisterDTO) , HttpStatus.OK);

    }
    @PutMapping("activation")
    public ResponseEntity<?> activateToken(@RequestParam UUID token)
    {
        return new ResponseEntity<>( customerService.activateCustomer(token) , HttpStatus.OK);
    }
    @PostMapping("resend-activation")
    public ResponseEntity<?> resendActivation(@Valid @RequestParam String email )
    {
        return new ResponseEntity<>(customerService.resendActivationToken(email) , HttpStatus.OK);

    }

    @GetMapping("show/profile")
    public ResponseEntity<?> showProfile()
    {
         return new ResponseEntity<>(customerService.showCustomer() , HttpStatus.OK);
    }

    @GetMapping("show/address")
    public ResponseEntity<?> showAddress()
    {
        return new ResponseEntity<>(customerService.showAddress() , HttpStatus.OK);
    }

    @PatchMapping("update/password")
    public ResponseEntity<?> updatePassword( @RequestBody UpdatePasswordDTO updatePasswordDTO)
    {
        return new ResponseEntity<>(customerService.updatePassword( updatePasswordDTO) , HttpStatus.OK);
    }

    @PatchMapping("update/profile")
    public ResponseEntity<?> updateProfile(@ModelAttribute UpdateCustomerDTO updateCustomerDTO
                                           ) throws IOException {
        return new ResponseEntity<>(customerService.updateProfile( updateCustomerDTO )
                , HttpStatus.OK);

    }

    @PutMapping("add/address")
    public ResponseEntity<?> addAddress( @RequestBody AddressDTO addressDTO)
    {
        return new ResponseEntity<>(customerService.addNewAddress(addressDTO)
                , HttpStatus.OK);
    }

    @DeleteMapping("delete/address/{id}")
    public ResponseEntity<?> deleteAddress( @PathVariable Long id)
    {
        return new ResponseEntity<>(customerService.deleteAddress(id)
                , HttpStatus.OK) ;
    }

    @PatchMapping("update/address/{id}")
    public ResponseEntity<?> updateAddress(@RequestBody AddressDTO addressDTO , @PathVariable Long id)
    {
        return new ResponseEntity<>(customerService.updateAddress( addressDTO , id)
                , HttpStatus.OK);
    }



}
