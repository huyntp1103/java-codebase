package com.example.blogapi.user.service;

import com.example.blogapi.common.exception.ResourceAlreadyExistsException;
import com.example.blogapi.common.exception.ResourceNotFoundException;
import com.example.blogapi.user.dto.UserRegistrationDto;
import com.example.blogapi.user.dto.UserResponseDto;
import com.example.blogapi.user.model.User;
import com.example.blogapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto registrationDto;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup test data
        registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setFullName("Test User");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .fullName("Test User")
                .build();
    }

    @Test
    void registerUser_ShouldReturnUserResponseDto() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserResponseDto result = userService.registerUser(registrationDto);

        // Then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFullName(), result.getFullName());
    }

    @Test
    void registerUser_WithExistingUsername_ShouldThrowException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.registerUser(registrationDto);
        });
    }

    @Test
    void getUserById_ShouldReturnUserResponseDto() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        UserResponseDto result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void getUserById_WithNonExistingId_ShouldThrowException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(99L);
        });
    }
}