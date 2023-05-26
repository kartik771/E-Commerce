package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.CustomerListDTO;
import com.Bootcamp_Project.ECommerce.dto.CustomerRegisterDTO;
import com.Bootcamp_Project.ECommerce.dto.MessageDTO;
import com.Bootcamp_Project.ECommerce.dto.SellerListDTO;
import com.Bootcamp_Project.ECommerce.entities.user.Customer;
import com.Bootcamp_Project.ECommerce.entities.user.Seller;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.exception.UserNotFoundException;
import com.Bootcamp_Project.ECommerce.repos.CustomerRepository;
import com.Bootcamp_Project.ECommerce.repos.SellerRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    public List<CustomerListDTO> showCustomers(Integer pageOffSet , Integer pageSize , String sortBy)
    {
        PageRequest page = PageRequest.of(pageOffSet , pageSize , Sort.Direction.ASC , sortBy);
        Page<Customer> customerPage = customerRepository.findAll(page);
        List<Customer> customers = customerRepository.findAll();
        List<CustomerListDTO> customerList = new ArrayList<>();
        for (Customer customer: customerPage) {
            CustomerListDTO customerListDTO = new CustomerListDTO();
            customerListDTO.setId(customer.getId());
            customerListDTO.setEmail(customer.getEmail());
            customerListDTO.setFullName(customer.getFirstName() +" " + customer.getMiddleName() + customer.getLastName());
            customerListDTO.setActive(customer.getIsActive());
            customerList.add(customerListDTO);
        }
        return customerList;
    }

    public List<SellerListDTO> showSellers(int pageOffSet , int pageSize , String sortBy)
    {
        PageRequest page = PageRequest.of(pageOffSet , pageSize , Sort.Direction.ASC , sortBy);
        Page<Seller> sellerPage = sellerRepository.findAll(page);
//        List<Seller> sellers = sellerRepository.findAll();
        List<SellerListDTO> sellerList = new ArrayList<>();
    for(Seller seller: sellerPage)
    {
        SellerListDTO sellerListDTO = new SellerListDTO();
        sellerListDTO.setId(seller.getId());
        sellerListDTO.setEmail(seller.getEmail());
        sellerListDTO.setFullName(seller.getFirstName()+ " "+ seller.getMiddleName()+ seller.getLastName());
        sellerListDTO.setCompanyName(seller.getCompanyName());
        sellerListDTO.setCompanyContact(seller.getCompanyContact());
        sellerListDTO.setGst(seller.getGst());
        sellerListDTO.setCompanyAddress(seller.getAddressList());
        sellerListDTO.setActive(seller.getIsActive());
        sellerList.add(sellerListDTO);
    }
        return sellerList;
    }



    public MessageDTO activateDeactivateUser(Long id)
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User adminUser = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The User does not exist!"));
        MessageDTO messageDTO = new MessageDTO();
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("The user does not exist with the given credentials! "));

        if(user == null)
        {
            throw new UserNotFoundException("No valid User Found");
        }

        if(!user.getIsActive())
        {
            user.setModifiedBy(adminUser.getFirstName());
            user.setIsActive(true);
            userRepository.save(user);
            String body = "Your Account has been activated";
            emailSenderService.sendSimpleEmail(user.getEmail() , body , "ACTIVATION MAIL");
            messageDTO.setMessage("The user has been Activated Successfully" );
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }

        user.setModifiedBy(adminUser.getFirstName());
        user.setIsActive(false);
        userRepository.save(user);
        String body = "Your Account has been deactivated";
        emailSenderService.sendSimpleEmail(user.getEmail() , body , "ACCOUNT DEACTIVATED");

        messageDTO.setMessage("The user has been deactivated Successfully" );
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }


    public MessageDTO lockUnlockUser(Long id)
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User adminUser = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The User does not exist!"));
        MessageDTO messageDTO = new MessageDTO();
        User user = userRepository.findById(id).orElse(null);
        if(user == null)
        {
            throw new UserNotFoundException("No valid User Found");
        }
        if(user.getIsLocked())
        {
            user.setModifiedBy(adminUser.getFirstName());
            user.setIsLocked(false);
            user.setInvalidAttemptCount(0);
            userRepository.save(user);
            String body = "Your account has been unlocked";
            emailSenderService.sendSimpleEmail(user.getEmail() , body , "ACCOUNT UNLOCKED");
            messageDTO.setMessage("The User account has been unlocked successfully" );
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }
        user.setModifiedBy(adminUser.getFirstName());
        user.setIsLocked(true);
        userRepository.save(user);
        String body = "Your account has been locked";
        emailSenderService.sendSimpleEmail(user.getEmail() , body , "ACCOUNT LOCKED");
        messageDTO.setMessage("The User account has been locked successfully" );
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;
    }

}
