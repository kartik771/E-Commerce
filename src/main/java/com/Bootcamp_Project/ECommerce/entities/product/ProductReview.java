package com.Bootcamp_Project.ECommerce.entities.product;

import com.Bootcamp_Project.ECommerce.entities.compositeKeys.CustomerProductReviewCompositeKey;
import com.Bootcamp_Project.ECommerce.entities.product.Product;
import com.Bootcamp_Project.ECommerce.entities.user.Customer;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ProductReview {

    @EmbeddedId
    private CustomerProductReviewCompositeKey compositeKey;

    @ManyToOne
    @MapsId("product")
    private Product product;

    @ManyToOne
    @MapsId("customer")
    private Customer customer;

    private String review;
    private Double rating;
    
}
