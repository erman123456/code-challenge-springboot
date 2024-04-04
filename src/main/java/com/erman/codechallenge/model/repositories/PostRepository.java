package com.erman.codechallenge.model.repositories;

import com.erman.codechallenge.model.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    PostEntity findById(Integer id);
    Optional<PostEntity> findByTitle(String title);

}
