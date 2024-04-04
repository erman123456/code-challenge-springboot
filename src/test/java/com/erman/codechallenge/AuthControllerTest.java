package com.erman.codechallenge;

import com.erman.codechallenge.dtos.WebResponse;
import com.erman.codechallenge.dtos.login.LoginRequest;
import com.erman.codechallenge.dtos.login.LoginResponse;
import com.erman.codechallenge.model.entities.UserEntity;
import com.erman.codechallenge.model.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("test");

        mockMvc.perform(post("/v1/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpectAll(status().isUnauthorized());
    }

    @Test
    void loginFailedUserWrongPassword() throws Exception {

        UserEntity user = new UserEntity();
        user.setName("ErmanTest");
        user.setUsername("ermanTest");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("ErmanTest");
        loginRequest.setPassword("test");

        mockMvc.perform(
                post("/v1/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
        });
    }

    @Test
    void loginSuccess() throws Exception {

        UserEntity user = new UserEntity();
        user.setName("ErmanTest");
        user.setUsername("ermanTest");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("ermanTest");
        loginRequest.setPassword("password");


        mockMvc.perform(
                post("/v1/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            System.out.println("responseSuccess : " + response);
            assertNull(response.getErrors());
            assertNotNull(response.getData().getAccessToken());
            assertNotNull(response.getData().getExpiredAt());
        });
    }
}
