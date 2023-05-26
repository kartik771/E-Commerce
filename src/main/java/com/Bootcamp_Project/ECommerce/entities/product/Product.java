package com.Bootcamp_Project.ECommerce.entities.product;


import com.Bootcamp_Project.ECommerce.entities.auditing.Auditable;
import com.Bootcamp_Project.ECommerce.entities.category.Category;
import com.Bootcamp_Project.ECommerce.entities.user.Seller;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Product extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "product_generator")
    @SequenceGenerator(name = "product_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;

    @ManyToOne
    private Seller seller;

    private String name;
    private String description;

    @ManyToOne
    private Category category;

    private boolean isCancellable ;
    private boolean isReturnable;
    private String brand;
    private boolean isActive ;
    private boolean isDeleted;

    @OneToMany(mappedBy = "product")
    private List<ProductVariation> productVariation;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReview;
}
