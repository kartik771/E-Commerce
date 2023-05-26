package com.Bootcamp_Project.ECommerce.entities.order;


import com.Bootcamp_Project.ECommerce.entities.product.ProductVariation;
import com.Bootcamp_Project.ECommerce.entities.user.Customer;
import com.Bootcamp_Project.ECommerce.entities.compositeKeys.CartCompositeKey;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
public class Cart {

    @EmbeddedId
    private CartCompositeKey cartCompositeKey;

    @OneToOne
    @MapsId("customer")
    private Customer customer;

    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Boolean isWishlist;

    @ManyToOne
    @MapsId("productVariation")
    private ProductVariation productVariation;
}
