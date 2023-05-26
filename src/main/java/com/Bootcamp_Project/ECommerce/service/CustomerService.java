package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.*;
import com.Bootcamp_Project.ECommerce.entities.contants.RoleConstants;
import com.Bootcamp_Project.ECommerce.entities.user.Address;
import com.Bootcamp_Project.ECommerce.entities.user.Customer;
import com.Bootcamp_Project.ECommerce.entities.user.Role;
import com.Bootcamp_Project.ECommerce.exception.UnSolvedException;
import com.Bootcamp_Project.ECommerce.exception.UserExistsException;
import com.Bootcamp_Project.ECommerce.repos.*;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Data
public class CustomerService {
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
    private EmailSenderService emailSenderService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Value("${project.image}")
    private String path;
    @Autowired
    private AddressRepository addressRepository;

    private MessageSource messageSource;

    public CustomerService (MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
    public MessageDTO createCustomer(CustomerRegisterDTO customerRegisterDTO) throws Exception{
        MessageDTO messageDTO = new MessageDTO();
        if(customerRepository.existsByEmail(customerRegisterDTO.getEmail()))
        {
            throw new UserExistsException("User Already exists");
        }
        else
        {
            Customer customer = new Customer();
            customer.setEmail(customerRegisterDTO.getEmail());
            if(customerRegisterDTO.getConfirmPassword().equals(customerRegisterDTO.getPassword())){
                customer.setPassword(passwordEncoder.encode(customerRegisterDTO.getPassword()));
            }
            else {
                throw new UnSolvedException("The password and confirm passwords do not match!");
            }
            customer.setFirstName(customerRegisterDTO.getFirstName());
            customer.setMiddleName(customerRegisterDTO.getMiddleName());
            customer.setLastName(customerRegisterDTO.getLastName());
            customer.setContact(customerRegisterDTO.getContact());

            List<Address> addresses = new ArrayList<>();
            for (Address address : customerRegisterDTO.getAddresses()) {
                address.setUser(customer);
                addresses.add(address);
            }
            customer.setAddressList(customerRegisterDTO.getAddresses());
            Role role = roleRepository.findByAuthority(RoleConstants.CUSTOMER).orElse(null);
            customer.setRoleList(Collections.singletonList(role));
            UUID token = UUID.fromString(UUID.randomUUID().toString());
            customer.setActivationToken(token);
            customer.setActivationTokenTime(LocalDateTime.now());
            customer.setActivationTokenExpirationTime(LocalDateTime.now().plusHours(3));
            customer.setCreatedBy(customer.getFirstName());
            userRepository.save(customer);
            String body = "To activate your account, please verify your email " +
                    "on given link " + "http://localhost:8080/customer/activation?token=" + token;

            emailSenderService.sendSimpleEmail(customer.getEmail(), body, "Activation Email");
            Locale locale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("customer.created.message" , null , "Default Message", locale);
            messageDTO.setMessage(message);
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }


    }

    public MessageDTO activateCustomer(UUID token)
    {
        MessageDTO messageDTO = new MessageDTO();
        Customer customer =customerRepository.findByActivationToken(token).orElse(null);
        if(customer == null)
        {
            throw new EntityNotFoundException("The token entered is invalid or expired." +
                    "Please check and try Again!");
        }
        if (customerRepository.existsByActivationToken(token)) {
            if (customer.getActivationTokenExpirationTime().isAfter(LocalDateTime.now())) {
                customer.setIsActive(true);
                customer.setIsLocked(false);
                userRepository.save(customer);
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("customer.activated.message" , null , "Default Message", locale);
                System.out.println(message);
                messageDTO.setMessage(message);
                messageDTO.setTimeStamp(LocalDateTime.now());
                return messageDTO;
            }else {

                    UUID activationToken = UUID.fromString(UUID.randomUUID().toString());
                    customer.setActivationTokenExpirationTime(LocalDateTime.now().plusHours(3));
                    customer.setActivationTokenTime(LocalDateTime.now());
                    customer.setActivationToken(activationToken);
                    customerRepository.save(customer);
                    String body = "To activate your account, please verify your email " +
                            "on given link " + "http://localhost:8080/customer/activation?token=" + activationToken;
                    emailSenderService.sendSimpleEmail(customer.getEmail(), body , "Activation Email");
                messageDTO.setMessage("Token is expired. New Token is generated and sent.");
                messageDTO.setTimeStamp(LocalDateTime.now());
                return messageDTO;
                }
            }
        messageDTO.setMessage("Account is already Activated");
        messageDTO.setTimeStamp(LocalDateTime.now());
        return messageDTO;

    }
    public MessageDTO resendActivationToken(String email)
    {
        MessageDTO messageDTO = new MessageDTO();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Email does not exist"));
        if (customerRepository.existsByEmail(email) && !customer.getIsActive() ) {
            UUID token1 = UUID.randomUUID();
            customer.setActivationToken(token1);
            customer.setActivationTokenTime(LocalDateTime.now());
            customer.setActivationTokenExpirationTime(LocalDateTime.now().plusHours(3));
            customerRepository.save(customer);
            String body = "To activate your account, please verify your email " +
                    "on given link " + "http://localhost:8080/customer/activation?token=" + token1;
            emailSenderService.sendSimpleEmail(customer.getEmail(), body , "Activation Email");
            messageDTO.setMessage("The Activation link is sent again");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }
        else {
            throw new EntityNotFoundException("Account is already activated");

        }
    }

    public CustomerDTO showCustomer()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
        CustomerDTO customerDTO = new CustomerDTO();
        if(userRepository.existsByEmail(email)) {
            customerDTO.setId(customer.getId());
            customerDTO.setFirstName(customer.getFirstName());
            customerDTO.setLastName(customer.getLastName());
            customerDTO.setContact(customer.getContact());
            customerDTO.setActive(customer.getIsActive());
//            customerDTO.setImage(imageService.getResource(path, ));
            return customerDTO;
        }
        else
        {
            throw new EntityNotFoundException( "Enter the valid credentials");
        }

    }

    public List<AddressDTO> showAddress()
    {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
        if(userRepository.existsByEmail(email))
        {
            List<Address> address = customer.getAddressList();
            List<AddressDTO> addressDTOList = new ArrayList<>();
            for (Address addresses : address) {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setId(addresses.getId());
                addressDTO.setAddressLine(addresses.getAddressLine());
                addressDTO.setCity(addresses.getCity());
                addressDTO.setState(addresses.getState());
                addressDTO.setCountry(addresses.getCountry());
                addressDTO.setZipCode(addresses.getZipCode());
                addressDTO.setLabel(addresses.getLabel());
                addressDTOList.add(addressDTO);
            }
            return addressDTOList;
        }
        else
        {
            throw new EntityNotFoundException("No valid Information Found");
        }

    }

    public MessageDTO updatePassword( UpdatePasswordDTO updatePasswordDTO) {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
            if (updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmPassword())) {
                customer.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
                customer.setPasswordUpdateDate(LocalDateTime.now());
                customer.setModifiedBy(customer.getFirstName());
                userRepository.save(customer);
                String body = "Your Password has  been updated successfully. ";
                emailSenderService.sendSimpleEmail(email, body, "PASSWORD UPDATED");
                Locale locale = LocaleContextHolder.getLocale();
                String message = messageSource.getMessage("password.update.message" , null , "Default Message", locale);
                messageDTO.setMessage(message);
                messageDTO.setTimeStamp(LocalDateTime.now());
                return messageDTO;
            }
        throw new EntityNotFoundException("Token does not exist");

    }

    public MessageDTO updateProfile( UpdateCustomerDTO updateCustomerDTO ) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
        MessageDTO messageDTO = new MessageDTO();
        if(customerRepository.existsByEmail(email)) {
            if (updateCustomerDTO.getFirstName() != null) {
                customer.setFirstName(updateCustomerDTO.getFirstName());
            }
            if (updateCustomerDTO.getMiddleName() != null) {
                customer.setMiddleName(updateCustomerDTO.getMiddleName());
            }
            if (updateCustomerDTO.getLastName() != null) {
                customer.setLastName(updateCustomerDTO.getLastName());
            }
            if (updateCustomerDTO.getContact() != null) {
                customer.setContact(updateCustomerDTO.getContact());
            }
            if(updateCustomerDTO.getImage()!=null)
            {
                imageService.uploadImage(path, updateCustomerDTO.getImage() , customer.getId());
            }
            customerRepository.save(customer);
            messageDTO.setMessage("The Profile details are updated successfully");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }

       throw new EntityNotFoundException("No valid user details found");
    }

    public MessageDTO addNewAddress(AddressDTO addressDTO) {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
        List<Address> addresses = customer.getAddressList();
        if(customerRepository.existsByEmail(email))
        {
            Address address = new Address();
            address.setAddressLine(addressDTO.getAddressLine());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setCountry(addressDTO.getCountry());
            address.setZipCode(addressDTO.getZipCode());
            address.setLabel(addressDTO.getLabel());
            address.setUser(customer);
            addresses.add(address);
            addressRepository.save(address);
            messageDTO.setTimeStamp(LocalDateTime.now());
            messageDTO.setMessage("The address of the user has been updated successfully");
            return messageDTO;
        }
        throw new EntityNotFoundException("No valid user details were found");
    }

    public MessageDTO deleteAddress(Long id)
    {
        MessageDTO messageDTO = new MessageDTO();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
        if(customerRepository.existsByEmail(email)) {
            addressRepository.deleteById(id);
            messageDTO.setMessage("The user address has been deleted successfully");
            messageDTO.setTimeStamp(LocalDateTime.now());
            return messageDTO;
        }
        throw new EntityNotFoundException("No valid user details were found");
    }

    public MessageDTO updateAddress(AddressDTO addressDTO , Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("The customer does not exist"));
        Address address = addressRepository.findById(id).orElseThrow(()->new EntityNotFoundException("address Not found"));
        MessageDTO messageDTO = new MessageDTO();

            if(!Objects.isNull(addressDTO.getAddressLine()))
            {
                address.setAddressLine(addressDTO.getAddressLine());
            }
            if(!Objects.isNull(addressDTO.getCity())){
                address.setCity(addressDTO.getCity());
            }
            if(!Objects.isNull(addressDTO.getState())) {
                address.setState(addressDTO.getState());
            }
            if(!Objects.isNull(addressDTO.getCountry())) {
                address.setCountry(addressDTO.getCountry());
            }
            if(!Objects.isNull(addressDTO.getZipCode())) {
                address.setZipCode(addressDTO.getZipCode());
            }
            if(!Objects.isNull(addressDTO.getLabel()))
            {
                address.setLabel(addressDTO.getLabel());
            }

            addressRepository.save(address);
            messageDTO.setMessage("The address has been updated successfully");
            messageDTO.setTimeStamp(LocalDateTime.now());
         return messageDTO;

    }
}
