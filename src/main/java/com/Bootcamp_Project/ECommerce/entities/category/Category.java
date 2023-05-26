package com.Bootcamp_Project.ECommerce.entities.category;



import com.Bootcamp_Project.ECommerce.entities.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Category  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "category_generator")
    @SequenceGenerator(name = "category_generator" , sequenceName = "user_generator_seq" , initialValue = 1 , allocationSize = 1)
    private Long id;
    private String name;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentCategoryId" )
    @JsonIgnore
    private Category parentCategory;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues;
}
