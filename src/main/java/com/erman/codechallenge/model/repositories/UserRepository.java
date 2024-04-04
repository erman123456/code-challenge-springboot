package com.erman.codechallenge.model.repositories;

import com.erman.codechallenge.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findById(Integer id);
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);


}
