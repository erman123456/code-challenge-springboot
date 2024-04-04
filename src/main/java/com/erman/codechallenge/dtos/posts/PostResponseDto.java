package com.erman.codechallenge.dtos.posts;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
   private Integer id;
   private String title;
   private String body;
   private String category;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
}
