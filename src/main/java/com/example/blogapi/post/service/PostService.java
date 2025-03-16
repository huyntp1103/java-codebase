package com.example.blogapi.post.service;

import com.example.blogapi.common.exception.ResourceNotFoundException;
import com.example.blogapi.post.dto.PostCreateDto;
import com.example.blogapi.post.dto.PostResponseDto;
import com.example.blogapi.post.model.Post;
import com.example.blogapi.post.repository.PostRepository;
import com.example.blogapi.user.dto.UserResponseDto;
import com.example.blogapi.user.model.User;
import com.example.blogapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponseDto createPost(Long userId, PostCreateDto postCreateDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Post post = Post.builder()
                .title(postCreateDto.getTitle())
                .content(postCreateDto.getContent())
                .author(author)
                .build();

        Post savedPost = postRepository.save(post);

        return mapToResponseDto(savedPost);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<PostResponseDto> getPostsByUser(Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return postRepository.findByAuthorOrderByCreatedAtDesc(author)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        return mapToResponseDto(post);
    }

    private PostResponseDto mapToResponseDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(mapUserToDto(post.getAuthor()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private UserResponseDto mapUserToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}