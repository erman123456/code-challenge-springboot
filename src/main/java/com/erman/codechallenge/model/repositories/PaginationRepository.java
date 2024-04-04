package com.erman.codechallenge.model.repositories;

import com.erman.codechallenge.model.entities.PostEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaginationRepository extends PagingAndSortingRepository<PostEntity, Integer> {
}
