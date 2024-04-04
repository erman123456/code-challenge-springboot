package com.erman.codechallenge.controllers;

import com.erman.codechallenge.dtos.WebResponse;
import com.erman.codechallenge.dtos.login.LoginRequest;
import com.erman.codechallenge.dtos.login.LoginResponse;
import com.erman.codechallenge.dtos.registers.RegisterRequest;
import com.erman.codechallenge.dtos.registers.RegisterResponse;
import com.erman.codechallenge.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse loginResponse = authService.login(request);
        return WebResponse.<LoginResponse>builder().data(loginResponse).build();
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<RegisterResponse> register(@RequestBody RegisterRequest request){
       RegisterResponse registerResponse = authService.register(request);
       return WebResponse.<RegisterResponse>builder().data(registerResponse).build();
    }

    @GetMapping("/test")
    @Secured("ROLE_VIEWER")
    public String getUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }

}
