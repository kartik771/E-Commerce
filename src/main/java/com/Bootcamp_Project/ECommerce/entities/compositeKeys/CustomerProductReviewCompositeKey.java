package com.Bootcamp_Project.ECommerce.entities.compositeKeys;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;


@Data
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class CustomerProductReviewCompositeKey implements Serializable {

    private Long customer;
    private Long product;

}
