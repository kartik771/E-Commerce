package com.Bootcamp_Project.ECommerce.entities.category;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class CategoryMetadataField {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "category_metadata_generator")
    @SequenceGenerator(name = "category_metadata_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "categoryMetadataField")
    private Set<CategoryMetadataFieldValues> categoryMetadataFieldValues;
}
