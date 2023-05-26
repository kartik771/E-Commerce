package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role , Long> {
    Optional<Role> findByAuthority(String authority);
    Boolean existsByAuthority(String authority);
}
