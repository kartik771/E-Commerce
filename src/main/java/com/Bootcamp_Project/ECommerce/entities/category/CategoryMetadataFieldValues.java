package com.Bootcamp_Project.ECommerce.entities.category;

import com.Bootcamp_Project.ECommerce.entities.compositeKeys.CategoryMetadataCompositeKey;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryMetadataCompositeKey compositeKey;

    @ManyToOne
    @MapsId("category")
    private Category category;

    @ManyToOne
    @MapsId("categoryMetadataField")
    private CategoryMetadataField categoryMetadataField;

    private String fieldValues;


}
