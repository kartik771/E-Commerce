package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.AuthResponseDTO;
import com.Bootcamp_Project.ECommerce.dto.LoginDTO;
import com.Bootcamp_Project.ECommerce.dto.MessageDTO;
import com.Bootcamp_Project.ECommerce.entities.user.Tokens;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.exception.UserLockedException;
import com.Bootcamp_Project.ECommerce.exception.UserNotFoundException;
import com.Bootcamp_Project.ECommerce.repos.CustomerRepository;
import com.Bootcamp_Project.ECommerce.repos.RoleRepository;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import com.Bootcamp_Project.ECommerce.security.SecurityConstants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class LoginService {
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
        public MessageDTO customerLogin(LoginDTO loginDTO)
    {
        MessageDTO messageDTO = new MessageDTO();
        User user = userRepository.findByEmail(loginDTO.getEmail()).orElse(null);
        if(user == null)
        {
            throw new EntityNotFoundException("User Does Not exist");
        }
        if (user.getIsActive() && !user.getIsLocked()) {

            if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                if (user.getInvalidAttemptCount() == 3) {
                    user.setIsLocked(true);
                    user.setIsDeleted(false);
                    userRepository.save(user);
                    throw new UserLockedException("Account is Locked due to 3 unsuccessful login attempts");
                } else {
                    user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);

                    userRepository.save(user);
                    throw new UserNotFoundException("Please Enter the valid account details");
                }


            } else {
                user.setInvalidAttemptCount(0);
                boolean tokenExists = tokenRepository.existsByEmail(loginDTO.getEmail());
                System.out.println(tokenExists);

                if(!tokenExists)
                {
                    String accessToken = jwtGenerator.generateToken(loginDTO.getEmail(), SecurityConstants.JWT_EXPIRATION_ACCESS);
                    Tokens token = new Tokens();
                    token.setToken(accessToken);
                    token.setUser(user);
                    token.setDeleted(false);
                    token.setExpired(false);
                    token.setEmail(loginDTO.getEmail());
                    tokenRepository.save(token);
                    Authentication authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    messageDTO.setMessage("User Has Logged in successfully");
                    messageDTO.setTimeStamp(LocalDateTime.now());
                    AuthResponseDTO authResponseDTO = new AuthResponseDTO(accessToken);
                    messageDTO.setToken(authResponseDTO.getAccessToken());
                    messageDTO.setTokenType(authResponseDTO.getTokenType());
                    return messageDTO;
                }
                else
                {
                    Tokens accessToken = tokenRepository.findByEmail(loginDTO.getEmail()).orElse(null);
                    System.out.println(accessToken.getToken());
                    messageDTO.setMessage("User Has Logged in successfully");
                    messageDTO.setTimeStamp(LocalDateTime.now());
                    AuthResponseDTO authResponseDTO = new AuthResponseDTO(accessToken.getToken());
                    messageDTO.setToken(authResponseDTO.getAccessToken());
                    messageDTO.setTokenType(authResponseDTO.getTokenType());
                    return messageDTO;
                }

            }
        }
        messageDTO.setMessage("The Account of user is either Locked or Deactivated");
        messageDTO.setTimeStamp(LocalDateTime.now());

        return messageDTO;
    }
}
