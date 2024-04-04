package com.erman.codechallenge.services;

import com.erman.codechallenge.dtos.posts.PaginationRequestDto;
import com.erman.codechallenge.dtos.posts.PostRequestDto;
import com.erman.codechallenge.dtos.posts.PostResponseDto;
import com.erman.codechallenge.model.entities.PostEntity;
import com.erman.codechallenge.model.repositories.PaginationRepository;
import com.erman.codechallenge.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PaginationRepository paginationRepository;

    @Autowired
    public PostService(PostRepository postRepository, PaginationRepository paginationRepository) {

        this.postRepository = postRepository;
        this.paginationRepository = paginationRepository;
    }

    public List<PostResponseDto> getAllPos(PaginationRequestDto paginationRequestDto) {
        final Pageable pageable;
        if (paginationRequestDto.getSortByDirection().equalsIgnoreCase("asc")) {
            pageable = PageRequest.of(paginationRequestDto.getPageNumber(), paginationRequestDto.getPageSize(), Sort.by(paginationRequestDto.getSortByField()).ascending());
        } else {
            pageable = PageRequest.of(paginationRequestDto.getPageNumber(), paginationRequestDto.getPageSize(), Sort.by(paginationRequestDto.getSortByField()).descending());
        }
        Page<PostEntity> allPosts = paginationRepository.findAll(pageable);
        return allPosts.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto) {

        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postRequestDto.getTitle());
        postEntity.setBody(postRequestDto.getBody());
        postEntity.setCategory(postRequestDto.getCategory());
        postEntity.setCreatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        postEntity.setUpdatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        PostEntity postEntityResult = postRepository.save(postEntity);

        return convertToUserResponse(postEntityResult);
    }

    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long id) {

        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        postEntity.setTitle(postRequestDto.getTitle());
        postEntity.setBody(postRequestDto.getBody());
        postEntity.setCategory(postRequestDto.getCategory());
        postEntity.setUpdatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        PostEntity postEntityResult = postRepository.save(postEntity);

        return convertToUserResponse(postEntityResult);
    }

    public PostEntity findById(Long id) {
        Optional<PostEntity> supplier = postRepository.findById(id);
        return supplier.orElse(null);
    }


    public PostResponseDto convertToUserResponse(PostEntity post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
