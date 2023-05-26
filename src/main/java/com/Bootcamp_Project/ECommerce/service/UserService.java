package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.ForgotPasswordDTO;
import com.Bootcamp_Project.ECommerce.entities.user.Tokens;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.repos.CustomerRepository;
import com.Bootcamp_Project.ECommerce.repos.RoleRepository;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import com.Bootcamp_Project.ECommerce.security.SecurityConstants;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Data
public class UserService {
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
    private CustomerRepository customerRepository;
    @Autowired
    private TokenRepository tokenRepository;

    public ResponseEntity<?> sendForgotPasswordLink(String email)
    {
        if(userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("No valid details found"));
            if (user.getIsActive()) {

                UUID forgotPasswordToken = UUID.randomUUID();
                user.setForgotPasswordToken(forgotPasswordToken);
                user.setForgotPasswordTokenTime(LocalDateTime.now());
                user.setForgotPasswordTokenExpirationTime(LocalDateTime.now().plusMinutes(15));
                userRepository.save(user);
                String body = "To Change your Password Click the following link " +
                        "on given link " + "http://localhost:8080/auth/resetpassword?token=" +
                        forgotPasswordToken;
                emailSenderService.sendSimpleEmail(email, body, "Forgot Password Email");
                return ResponseEntity.ok("Password Reset Link is sent");

            }
        }
        return new ResponseEntity<>("Forgot Password Link is Sent Successfully" , HttpStatus.OK);
    }

    public ResponseEntity<?> resetPassword(UUID token , ForgotPasswordDTO forgotPasswordDTO ){
            if(userRepository.existsByForgotPasswordToken(token))
            {
                User user = userRepository.findByForgotPasswordToken(token).get();
                if(user.getForgotPasswordTokenExpirationTime().isAfter(LocalDateTime.now()))
                {
                    String email = user.getEmail();
                    if(forgotPasswordDTO.getPassword().equals(forgotPasswordDTO.getConfirmPassword()))
                    {
                        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getPassword()));
                        user.setPasswordUpdateDate(LocalDateTime.now());
                        userRepository.save(user);
                        String body = "Your Password has been updated. ";
                        emailSenderService.sendSimpleEmail(user.getEmail(), body , "Password is Updated");
                    }
                }
            }
            return new ResponseEntity<>("The Password has been updated" , HttpStatus.OK);


    }

}
