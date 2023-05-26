package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetadataField , Long> {
    Boolean existsByName(String name);

    Boolean existsByNameIgnoreCase(String key);

    CategoryMetadataField findByNameIgnoreCase(String key);
}
