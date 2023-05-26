package com.Bootcamp_Project.ECommerce.entities.compositeKeys;


import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
public class CategoryMetadataCompositeKey implements Serializable {

    private Long categoryMetadataField;
    private Long category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryMetadataCompositeKey that = (CategoryMetadataCompositeKey) o;
        return Objects.equals(categoryMetadataField, that.categoryMetadataField) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryMetadataField, category);
    }



   }
