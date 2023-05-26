package com.Bootcamp_Project.ECommerce.entities.compositeKeys;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class CartCompositeKey implements Serializable {

    private Long customer;
    private Long productVariation;

}
