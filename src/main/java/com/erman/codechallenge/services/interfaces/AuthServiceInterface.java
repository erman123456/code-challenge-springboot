package com.erman.codechallenge.services.interfaces;

import com.erman.codechallenge.dtos.login.LoginRequest;
import com.erman.codechallenge.dtos.login.LoginResponse;
import com.erman.codechallenge.dtos.registers.RegisterRequest;
import com.erman.codechallenge.dtos.registers.RegisterResponse;

public interface AuthServiceInterface {

    LoginResponse login(LoginRequest loginRequest);
    RegisterResponse register(RegisterRequest registerRequest);
}
