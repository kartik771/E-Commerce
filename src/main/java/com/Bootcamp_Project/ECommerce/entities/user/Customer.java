package com.Bootcamp_Project.ECommerce.entities.user;


import com.Bootcamp_Project.ECommerce.entities.order.Cart;
import com.Bootcamp_Project.ECommerce.entities.order.Orders;
import com.Bootcamp_Project.ECommerce.entities.product.ProductReview;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {

    private String contact;

    @OneToMany(mappedBy = "customer")
    private List<ProductReview> productReview;

    @OneToMany(mappedBy = "customer")
    private List<Cart> cart;

    @OneToMany(mappedBy = "customer")
    private List<Orders> orders;

    private UUID activationToken;
    private LocalDateTime activationTokenTime;
    private LocalDateTime activationTokenExpirationTime;



}
