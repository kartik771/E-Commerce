package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.*;
import com.Bootcamp_Project.ECommerce.entities.contants.RoleConstants;
import com.Bootcamp_Project.ECommerce.entities.user.Address;
import com.Bootcamp_Project.ECommerce.entities.user.Role;
import com.Bootcamp_Project.ECommerce.entities.user.Seller;
import com.Bootcamp_Project.ECommerce.entities.user.Tokens;
import com.Bootcamp_Project.ECommerce.exception.UserExistsException;
import com.Bootcamp_Project.ECommerce.exception.UserNotFoundException;
import com.Bootcamp_Project.ECommerce.repos.*;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Service
public class SellerService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private AddressRepository addressRepository;

    public MessageDTO createSeller(SellerRegisterDTO sellerRegisterDTO) throws UserExistsException {
        MessageDTO messageDTO = new MessageDTO();
        if (sellerRepository.existsByEmail(sellerRegisterDTO.getEmail())) {
            throw new UserExistsException("Email is already registered");
        }
        Seller seller = new Seller();
        seller.setEmail(sellerRegisterDTO.getEmail());
        seller.setPassword(passwordEncoder.encode(sellerRegisterDTO.getPassword()));
        seller.setFirstName(sellerRegisterDTO.getFirstName());
        seller.setMiddleName(sellerRegisterDTO.getMiddleName());
        seller.setLastName(sellerRegisterDTO.getLastName());
        seller.setGst(sellerRegisterDTO.getGst());
        seller.setCompanyName(sellerRegisterDTO.getCompanyName());
        seller.setCompanyContact(sellerRegisterDTO.getCompanyContact());
        sellerRegisterDTO.getCompanyAddress().setUser(seller);
        seller.setAddressList(Collections.singletonList(sellerRegisterDTO.getCompanyAddress()));
        Role role = roleRepository.findByAuthority(RoleConstants.SELLER).orElse(null);
        seller.setRoleList(Collections.singletonList(role));
        userRepository.save(seller);
        messageDTO.setMessage("The Seller is registered successfully!");
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

    public SellerDTO showProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The Seller does not exists"));
        if (sellerRepository.existsByEmail(email)) {
            SellerDTO sellerList = new SellerDTO();
            sellerList.setId(seller.getId());
            sellerList.setFirstName(seller.getFirstName());
            sellerList.setLastName(seller.getLastName());
            sellerList.setCompanyName(seller.getCompanyName());
            sellerList.setCompanyContact(seller.getCompanyContact());
            sellerList.setEmail(seller.getEmail());
            sellerList.setGst(seller.getGst());
            sellerList.setActive(seller.getIsActive());
            sellerList.setCompanyAddress(seller.getAddressList());


            return sellerList;
        }
        throw new UserNotFoundException("No valid user exists");

    }

    public MessageDTO updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The Seller does not exists"));
        MessageDTO messageDTO = new MessageDTO();
        if (sellerRepository.existsByEmail(email)) {
            if (updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmPassword())) {
                seller.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
                seller.setPasswordUpdateDate(LocalDateTime.now());
                userRepository.save(seller);
                String body = "Your Password has  been updated successfully. ";
                emailSenderService.sendSimpleEmail(email, body, "PASSWORD UPDATED");
                messageDTO.setMessage("Your Password has  been updated successfully. ");
                messageDTO.setTimeStamp(LocalDateTime.now());
                return messageDTO;
            }
        }

        throw new UserNotFoundException("No valid user exists");
    }

    public MessageDTO updateProfile(UpdateSellerProfileDTO updateSellerProfileDTO) {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The Seller does not exists"));
        if (sellerRepository.existsByEmail(email)) {
            if (updateSellerProfileDTO.getFirstName() != null) {
                seller.setFirstName(updateSellerProfileDTO.getFirstName());
            }
            if (updateSellerProfileDTO.getMiddleName() != null) {
                seller.setMiddleName(updateSellerProfileDTO.getMiddleName());
            }
            if (updateSellerProfileDTO.getLastName() != null) {
                seller.setLastName(updateSellerProfileDTO.getLastName());
            }
            if (updateSellerProfileDTO.getEmail() != null) {
                seller.setEmail(updateSellerProfileDTO.getEmail());
            }
            if (updateSellerProfileDTO.getCompanyName() != null) {
                seller.setCompanyName(updateSellerProfileDTO.getCompanyName());
            }
            if (updateSellerProfileDTO.getCompanyContact() != null) {
                seller.setCompanyContact(updateSellerProfileDTO.getCompanyContact());
            }
            if (!Objects.isNull(updateSellerProfileDTO.getGst())) {
                seller.setGst(updateSellerProfileDTO.getGst());
            }

            userRepository.save(seller);
            messageDTO.setMessage("The Seller profile has been updated successfully");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }
        throw new UserNotFoundException("No valid user exists");

    }

    public MessageDTO updateSellerAddress(Long id, AddressDTO addressDTO) {
        MessageDTO messageDTO = new MessageDTO();
        Address address = addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("address Not found"));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The Seller does not exists"));
        if (sellerRepository.existsByEmail(email)) {
            {

                if (!Objects.isNull(addressDTO.getAddressLine())) {
                    address.setAddressLine(addressDTO.getAddressLine());
                }
                if (!Objects.isNull(addressDTO.getCity())) {
                    address.setCity(addressDTO.getCity());
                }
                if (!Objects.isNull(addressDTO.getState())) {
                    address.setState(addressDTO.getState());
                }
                if (!Objects.isNull(addressDTO.getCountry())) {
                    address.setCountry(addressDTO.getCountry());
                }
                if (!Objects.isNull(addressDTO.getZipCode())) {
                    address.setZipCode(addressDTO.getZipCode());
                }

                addressRepository.save(address);
                messageDTO.setMessage("The seller address has been updated successfully");
                messageDTO.setTimeStamp(LocalDateTime.now());
                return messageDTO;
            }

        }
        throw new UserNotFoundException("No valid user exists");

    }
}
