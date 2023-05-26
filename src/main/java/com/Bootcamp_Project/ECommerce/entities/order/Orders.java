package com.Bootcamp_Project.ECommerce.entities.order;

import com.Bootcamp_Project.ECommerce.entities.order.OrderProduct;
import com.Bootcamp_Project.ECommerce.entities.user.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "orders_generator")
    @SequenceGenerator(name = "orders_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;

    @ManyToOne
    private Customer customer;

    private Double amountPaid;
    private LocalDate dateCreated;
    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressLine;
    private Integer customerAddressZipCode;
    private String customerAddressLabel;

    @OneToMany(mappedBy = "orders")
    private List<OrderProduct> orderProduct ;
}
