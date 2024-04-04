package com.erman.codechallenge.controllers;

import com.erman.codechallenge.dtos.WebResponse;
import com.erman.codechallenge.dtos.users.UserRequestDto;
import com.erman.codechallenge.dtos.users.UserResponseDto;
import com.erman.codechallenge.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> userResponse = userService.getAllUser();
        return WebResponse.<List<UserResponseDto>>builder().data(userResponse).build();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponseDto> createUser(@RequestBody UserRequestDto userRequest){
        UserResponseDto postResult =  userService.createUser(userRequest);
        return WebResponse.<UserResponseDto>builder().data(postResult).build();
    }
}
