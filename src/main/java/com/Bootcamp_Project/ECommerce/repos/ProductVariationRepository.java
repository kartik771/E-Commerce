package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.product.Product;
import com.Bootcamp_Project.ECommerce.entities.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariationRepository extends JpaRepository<ProductVariation , Long> {
    List<ProductVariation> findByProduct(Product product);

    @Query(value = "select min(price) from product_variation where product_id in " +
            "(select p.id from product p join category c on p.category_id=:categoryId)",nativeQuery = true)
    Long minimumPrice(@Param("categoryId") long id);

    @Query(value = "select max(price) from product_variation where product_id in " +
            "(select p.id from product p join category c on p.category_id=:categoryId)",nativeQuery = true)
    Long maximumPrice(@Param("categoryId") long id);
}
