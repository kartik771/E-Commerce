package com.Bootcamp_Project.ECommerce.entities.user;


import com.Bootcamp_Project.ECommerce.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class   Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "address_generator")
    @SequenceGenerator(name = "address_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;
    private String label;

    @ManyToOne
    @JsonIgnore
    private User user;

//    @OneToOne
//    private Seller seller;



}
