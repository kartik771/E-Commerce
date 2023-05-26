package com.Bootcamp_Project.ECommerce.service;

import com.Bootcamp_Project.ECommerce.dto.MessageDTO;
import com.Bootcamp_Project.ECommerce.entities.user.Tokens;
import com.Bootcamp_Project.ECommerce.repos.TokenRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.security.JWTGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LogoutService {
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public MessageDTO logoutUser(String token)
    {
        Tokens tokens = tokenRepository.findByToken(token).orElse(null);
        MessageDTO messageDTO = new MessageDTO();
        if(tokens == null)
        {
            throw new EntityNotFoundException("User is already Logged out");
        }

        System.out.println(tokens.getToken());
        tokenRepository.deleteByToken(tokens.getToken());
        messageDTO.setMessage( "User Logged out Successfully");
        messageDTO.setTimeStamp(LocalDateTime.now());

        return messageDTO;
    }
}
