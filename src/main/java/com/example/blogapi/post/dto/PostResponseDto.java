package com.example.blogapi.post.dto;

import com.example.blogapi.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private UserResponseDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPublished;
}