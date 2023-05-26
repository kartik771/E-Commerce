package com.Bootcamp_Project.ECommerce.repos;

import com.Bootcamp_Project.ECommerce.entities.user.Tokens;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


import java.util.Optional;

public interface TokenRepository extends JpaRepository<Tokens, Long> {
 Optional<Tokens> findByToken(String token);
 Optional<Tokens> findByEmail(String email);
 Boolean existsByToken(String token);
 Boolean existsByEmail(String email);


 void deleteByToken(String token);
}
