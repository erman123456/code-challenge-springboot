package com.erman.codechallenge.services;

import com.erman.codechallenge.dtos.users.UserRequestDto;
import com.erman.codechallenge.dtos.users.UserResponseDto;
import com.erman.codechallenge.model.entities.UserEntity;
import com.erman.codechallenge.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDto.getName());
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setPassword(userRequestDto.getPassword());
        UserEntity userEntityResult = userRepository.save(userEntity);

        return convertToUserResponse(userEntityResult);
    }
    public List<UserResponseDto> getAllUser(){
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }


    public UserResponseDto convertToUserResponse(UserEntity user) {
        // Assuming User entity has fields username and name
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }
}
