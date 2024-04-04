package com.erman.codechallenge.utils;

import com.erman.codechallenge.dtos.users.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;


@Component
public class JwtUtil {
    private static final String SECRET_KEY = "ermanSecretKey";
    public static final long EXPIRATION_TIME = 86400000;

    public String generateToken(UserResponseDto userResponse) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .claim("users", userResponse)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static UserResponseDto extractSubject(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        // Extract the "user" claim as a LinkedHashMap
        LinkedHashMap<String, Object> userMap = claims.get("users", LinkedHashMap.class);

        return convertLinkedHashMapToObject(userMap, UserResponseDto.class);
    }

    // Convert LinkedHashMap to a specified Java class using Jackson's ObjectMapper
    private static <T> T convertLinkedHashMapToObject(LinkedHashMap<String, Object> map, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, valueType);
    }
}
