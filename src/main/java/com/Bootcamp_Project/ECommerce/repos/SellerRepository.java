package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.user.Seller;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller , Long> {
    Optional<Seller> findByEmail(String email);
    Boolean existsByEmail(String email);
}
