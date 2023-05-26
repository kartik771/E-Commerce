package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.category.Category;
import com.Bootcamp_Project.ECommerce.entities.product.Product;
import com.Bootcamp_Project.ECommerce.entities.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product , Long> {
    List<Product> findAllByNameIgnoreCase(String name);
//    List<Product> findAllBySellerIdAndNameIgnoreCase(Long id , String name);

    List<Product> findAllBySellerId(Long id);

    List<Product> findBySeller(Seller seller);

    List<Product> findByCategory(Category category);

    List<Product> findAllByCategory(Category category);
}
