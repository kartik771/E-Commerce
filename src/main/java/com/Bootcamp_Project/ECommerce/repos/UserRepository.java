package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Optional<User> findByAccessToken(String token);
    Boolean existsByAccessToken(String token);

    Optional<User> findByForgotPasswordToken(UUID token);
    Boolean existsByForgotPasswordToken(UUID Token);

    @Query(value = "select email from user where is_active = false" , nativeQuery = true)
    List<String> findAllNonActiveUser();


}
