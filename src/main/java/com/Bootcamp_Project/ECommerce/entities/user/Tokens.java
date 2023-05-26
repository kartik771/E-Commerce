package com.Bootcamp_Project.ECommerce.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Entity
@Data
@NoArgsConstructor
//@EnableJpaRepositories
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "token_generator")
    @SequenceGenerator(name = "token_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;
    private String token;
    private String email;
    private boolean expired;
    private boolean isDeleted;
//    private Boolean revoked;

    @OneToOne
    @JsonIgnore
    private User user;
}
