package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewProductVariationRepository extends JpaRepository<ProductVariation , Long> {
}
