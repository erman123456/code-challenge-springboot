package com.erman.codechallenge;

import com.erman.codechallenge.dtos.WebResponse;
import com.erman.codechallenge.dtos.login.LoginRequest;
import com.erman.codechallenge.dtos.login.LoginResponse;
import com.erman.codechallenge.dtos.posts.PostRequestDto;
import com.erman.codechallenge.dtos.posts.PostResponseDto;
import com.erman.codechallenge.model.entities.PostEntity;
import com.erman.codechallenge.model.entities.UserEntity;
import com.erman.codechallenge.model.repositories.PostRepository;
import com.erman.codechallenge.model.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.parser.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BlogPostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    private String accessToken = "";

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
            accessToken = response.getData().getAccessToken();
            assertNull(response.getErrors());
            assertNotNull(response.getData().getAccessToken());
            assertNotNull(response.getData().getExpiredAt());
        });
    }

    @Test
    void createPosts() throws Exception {
        loginSuccess();
        System.out.println("accessTokenLogin : " + accessToken);
        PostEntity post = new PostEntity();
        post.setTitle("titleTest");
        post.setBody("bodyTest");
        post.setCategory("categoryTest");
        post.setCreatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        post.setUpdatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        mockMvc.perform(
                        post("/v1/posts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + accessToken)
                                .content(objectMapper.writeValueAsString(post))
                ).andExpectAll(
                        status().isOk()
                )
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andDo(result -> {
                    WebResponse<PostResponseDto> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNull(response.getErrors());
                    assertNotNull(response.getData().getTitle());
                    assertNotNull(response.getData().getBody());
                });
    }

    @Test
    void getPosts() throws Exception {
        loginSuccess();
        System.out.println("accessTokenLogin : " + accessToken);
        PostEntity post = new PostEntity();
        post.setTitle("titleTest");
        post.setBody("bodyTest");
        post.setCategory("categoryTest");
        post.setCreatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        post.setUpdatedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        postRepository.save(post);


        mockMvc.perform(
                        get("/v1/posts")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + accessToken)
                                .param("pageNumber", "0")
                                .param("pageSize", "5")
                ).andExpectAll(
                        status().isOk()
                )
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
    }
}
