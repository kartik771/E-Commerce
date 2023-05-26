package com.Bootcamp_Project.ECommerce.bootstrap;

import com.Bootcamp_Project.ECommerce.entities.contants.RoleConstants;
import com.Bootcamp_Project.ECommerce.entities.user.Role;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.Bootcamp_Project.ECommerce.repos.RoleRepository;
import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Component
public class Bootstrap implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${admin.email}")
    private String email;
    @Value("${admin.password}")
    private String password;


    @Override
    public void run(String... args) throws Exception {
        if(!roleRepository.existsByAuthority(RoleConstants.ADMIN))
        {
            Role role = new Role();
            role.setAuthority(RoleConstants.ADMIN);
            roleRepository.save(role);
        }
        if(!roleRepository.existsByAuthority(RoleConstants.CUSTOMER))
        {
            Role role1 = new Role();
            role1.setAuthority(RoleConstants.CUSTOMER);
            roleRepository.save(role1);
        }
        if(!roleRepository.existsByAuthority(RoleConstants.SELLER))
        {
            Role role2 = new Role();
            role2.setAuthority(RoleConstants.SELLER);
            roleRepository.save(role2);
        }
        if(!userRepository.existsByEmail(email))
        {
            User user = new User();
            user.setEmail(email);
            user.setFirstName("Admin");
            user.setMiddleName("");
            user.setLastName("");
            user.setPassword(passwordEncoder.encode(password));
            user.setIsDeleted(false);
            user.setIsActive(true);
            user.setIsExpired(false);
            user.setIsLocked(false);
            user.setInvalidAttemptCount(0);
            user.setPasswordUpdateDate(LocalDateTime.now());
            user.setCreatedBy(user.getFirstName());
            user.setModifiedBy(user.getFirstName());

            Role role = roleRepository.findByAuthority(RoleConstants.ADMIN).orElse(null);
            user.setRoleList(List.of(role));
            userRepository.save(user);
        }
    }
}