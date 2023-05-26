package com.Bootcamp_Project.ECommerce.entities.order;


import com.Bootcamp_Project.ECommerce.entities.product.ProductVariation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
//@Inheritance(strategy = InheritanceType.JOINED)
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "order_product_generator")
    @SequenceGenerator(name = "order_product_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;

    @ManyToOne
    private Orders orders;

    private Integer quantity;
    private Double amount;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private ProductVariation productVariation;
}
