package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.category.CategoryMetadataFieldValues;
import com.Bootcamp_Project.ECommerce.entities.compositeKeys.CategoryMetadataCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues , CategoryMetadataCompositeKey>{
    Boolean existsByCategoryIdAndCategoryMetadataFieldId(Long id, Long metaId);
    CategoryMetadataFieldValues findByCategoryIdAndCategoryMetadataFieldId(Long id, Long metaId);
}
