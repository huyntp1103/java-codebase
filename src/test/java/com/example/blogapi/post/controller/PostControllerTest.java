package com.example.blogapi.post.controller;

import com.example.blogapi.post.dto.PostCreateDto;
import com.example.blogapi.post.dto.PostResponseDto;
import com.example.blogapi.post.service.PostService;
import com.example.blogapi.user.dto.UserResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PostResponseDto samplePostResponse;
    private PostCreateDto samplePostRequest;
    private List<PostResponseDto> samplePostResponseList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PostController(postService))
                .build();

        UserResponseDto userResponse = UserResponseDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        samplePostResponse = PostResponseDto.builder()
                .id(1L)
                .title("Test Post")
                .content("This is a test post content")
                .author(userResponse)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        samplePostRequest = new PostCreateDto();
        samplePostRequest.setTitle("Test Post");
        samplePostRequest.setContent("This is a test post content");

        samplePostResponseList = List.of(samplePostResponse);
    }

    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        when(postService.createPost(eq(1L), any(PostCreateDto.class))).thenReturn(samplePostResponse);

        mockMvc.perform(post("/api/posts/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(samplePostRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.content", is("This is a test post content")))
                .andExpect(jsonPath("$.author.id", is(1)))
                .andExpect(jsonPath("$.author.username", is("testuser")));
    }

    @Test
    void getAllPosts_ShouldReturnAllPosts() throws Exception {
        when(postService.getAllPosts()).thenReturn(samplePostResponseList);

        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Post")));
    }

    @Test
    void getPostById_ShouldReturnPost() throws Exception {
        when(postService.getPostById(1L)).thenReturn(samplePostResponse);

        mockMvc.perform(get("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.content", is("This is a test post content")));
    }

    @Test
    void getPostsByUser_ShouldReturnUserPosts() throws Exception {
        when(postService.getPostsByUser(1L)).thenReturn(samplePostResponseList);

        mockMvc.perform(get("/api/posts/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Post")))
                .andExpect(jsonPath("$[0].author.id", is(1)));
    }
}