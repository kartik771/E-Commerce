package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.user.Customer;
import com.Bootcamp_Project.ECommerce.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer , Long> {
    Optional<Customer> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Customer> findByActivationToken(UUID token);
    Boolean existsByActivationToken(UUID token);
    void deleteByActivationToken(String toke);

}
