package com.example.blogapi.post.service;

import com.example.blogapi.common.exception.ResourceNotFoundException;
import com.example.blogapi.post.dto.PostCreateDto;
import com.example.blogapi.post.dto.PostResponseDto;
import com.example.blogapi.post.model.Post;
import com.example.blogapi.post.repository.PostRepository;
import com.example.blogapi.user.model.User;
import com.example.blogapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User user;
    private Post post;
    private PostCreateDto postCreateDto;

    @BeforeEach
    void setUp() {
        // Setup test data
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        post = Post.builder()
                .id(1L)
                .title("Test Post")
                .content("This is a test post")
                .author(user)
                .build();

        postCreateDto = new PostCreateDto();
        postCreateDto.setTitle("Test Post");
        postCreateDto.setContent("This is a test post");
    }

    @Test
    void createPost_ShouldReturnPostResponseDto() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        PostResponseDto result = postService.createPost(1L, postCreateDto);

        // Then
        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
        assertEquals(post.getTitle(), result.getTitle());
        assertEquals(post.getContent(), result.getContent());
        assertEquals(user.getId(), result.getAuthor().getId());
    }

    @Test
    void createPost_WithNonExistingUser_ShouldThrowException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.createPost(99L, postCreateDto);
        });
    }

    @Test
    void getAllPosts_ShouldReturnListOfPostResponseDto() {
        // Given
        List<Post> posts = Arrays.asList(post);
        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(posts);

        // When
        List<PostResponseDto> result = postService.getAllPosts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(post.getId(), result.get(0).getId());
    }

    @Test
    void getPostById_ShouldReturnPostResponseDto() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // When
        PostResponseDto result = postService.getPostById(1L);

        // Then
        assertNotNull(result);
        assertEquals(post.getId(), result.getId());
    }

    @Test
    void getPostById_WithNonExistingId_ShouldThrowException() {
        // Given
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPostById(99L);
        });
    }
}