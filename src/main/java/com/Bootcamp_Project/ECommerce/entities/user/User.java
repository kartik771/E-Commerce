package com.Bootcamp_Project.ECommerce.entities.user;


import com.Bootcamp_Project.ECommerce.entities.auditing.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "user_generator")
    @SequenceGenerator(name = "user_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private Boolean isDeleted = Boolean.FALSE;
    private Boolean isActive =Boolean.FALSE;
    private Boolean isExpired=Boolean.FALSE;
    private Boolean isLocked=Boolean.FALSE;
    private Integer invalidAttemptCount =0;
    private LocalDateTime passwordUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roleList;


    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Address> addressList;


    @OneToOne(mappedBy = "user")
    private Tokens accessToken;

    private UUID forgotPasswordToken;
    private LocalDateTime forgotPasswordTokenTime;
    private LocalDateTime forgotPasswordTokenExpirationTime;

    @Transient
    @Column
    @ToString.Exclude
    List<GrantedAuthority> grantAuthorityList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
