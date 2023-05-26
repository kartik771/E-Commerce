package com.Bootcamp_Project.ECommerce.entities.product;


import com.Bootcamp_Project.ECommerce.entities.auditing.Auditable;
import com.Bootcamp_Project.ECommerce.entities.order.Cart;
import com.Bootcamp_Project.ECommerce.entities.order.OrderProduct;
import com.Bootcamp_Project.ECommerce.service.HashMapConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class ProductVariation extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "product_variation_generator")
    @SequenceGenerator(name = "product_variation_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;

    @ManyToOne
    private Product product;

    private Integer quantityAvailable;
    private Integer price;

//    @Column(columnDefinition = "jsonMetadata")
//    private Map<String , String> metadata;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Byte[] primaryImageName;

    private Boolean isActive;

    @OneToMany(mappedBy = "productVariation",cascade = CascadeType.ALL)
    private List<Cart> cart;

    @OneToMany(mappedBy = "productVariation")
    private List<OrderProduct> orderProduct;

    @Convert(converter = HashMapConverter.class)
    private Map<String, String> metadata;

    private Long sellerId;
}
