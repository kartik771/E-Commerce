package com.Bootcamp_Project.ECommerce.entities.user;


import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "role_generator")
    @SequenceGenerator(name = "role_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private String authority;

    @ManyToMany(mappedBy = "roleList")
    @JsonIgnore
//    @JoinTable(name = "User_Role" , joinColumns = @JoinColumn(name = "roleId" , referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "userId" , referencedColumnName = "id"))
    private List<User> userList;
}
