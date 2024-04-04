package com.erman.codechallenge.dtos.registers;


import com.erman.codechallenge.dtos.users.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private UserResponseDto user;

    private String accessToken;

    private Long expiredAt;
}
