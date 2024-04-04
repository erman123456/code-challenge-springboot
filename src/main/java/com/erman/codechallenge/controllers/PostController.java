package com.erman.codechallenge.controllers;

import com.erman.codechallenge.dtos.WebResponse;
import com.erman.codechallenge.dtos.posts.PaginationRequestDto;
import com.erman.codechallenge.dtos.posts.PostRequestDto;
import com.erman.codechallenge.dtos.posts.PostResponseDto;
import com.erman.codechallenge.model.entities.PostEntity;
import com.erman.codechallenge.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<PostResponseDto>> getPost(PaginationRequestDto paginationRequestDto) {
        System.out.println("PostController.getPost" + paginationRequestDto);
        List<PostResponseDto> postResponse = postService.getAllPos(paginationRequestDto);
        return WebResponse.<List<PostResponseDto>>builder().data(postResponse).build();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public WebResponse<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto){
        PostResponseDto postResult =  postService.createPost(postRequestDto);
        return WebResponse.<PostResponseDto>builder().data(postResult).build();
    }

    @PutMapping("/{id}/update")
    public WebResponse<PostResponseDto> updatePost(@RequestBody PostRequestDto postRequestDto, @PathVariable("id") Long id){
        PostResponseDto postResult =  postService.updatePost(postRequestDto, id);
        return WebResponse.<PostResponseDto>builder().data(postResult).build();
    }
    @GetMapping("/{id}")
    public PostEntity findById(@PathVariable("id") Long id){
        return postService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        postService.deleteById(id);
    }
}
