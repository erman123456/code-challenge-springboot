package com.erman.codechallenge.services;

import com.erman.codechallenge.dtos.login.LoginRequest;
import com.erman.codechallenge.dtos.login.LoginResponse;
import com.erman.codechallenge.dtos.registers.RegisterRequest;
import com.erman.codechallenge.dtos.registers.RegisterResponse;
import com.erman.codechallenge.dtos.users.UserResponseDto;
import com.erman.codechallenge.model.entities.UserEntity;
import com.erman.codechallenge.model.repositories.UserRepository;
import com.erman.codechallenge.services.interfaces.AuthServiceInterface;
import com.erman.codechallenge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements AuthServiceInterface {

    @Autowired
    ValidationService validationService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) {
        validationService.validate(loginRequest);

        UserEntity user = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username not registered"));

        if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            UserResponseDto userResponse = userService.convertToUserResponse(user);
            String accessToken = jwtUtil.generateToken(userResponse);

            return LoginResponse.builder().user(userResponse).accessToken(accessToken).expiredAt(JwtUtil.EXPIRATION_TIME).build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or Password wrong");
        }
    }


    public RegisterResponse register(RegisterRequest registerRequest) {
        validationService.validate(registerRequest);

        UserEntity user = new UserEntity();
        user.setName(registerRequest.getName());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt()));
        userRepository.save(user);

        UserResponseDto userResponse = userService.convertToUserResponse(user);
        String accessToken = jwtUtil.generateToken(userResponse);

        return RegisterResponse.builder().user(userResponse).accessToken(accessToken).expiredAt(JwtUtil.EXPIRATION_TIME).build();
    }

}
