package com.example.blogapi.user.controller;

import com.example.blogapi.common.security.jwt.JwtTokenProvider;
import com.example.blogapi.user.dto.UserRegistrationDto;
import com.example.blogapi.user.dto.UserResponseDto;
import com.example.blogapi.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    private UserResponseDto sampleUserResponse;
    private UserRegistrationDto sampleRegistrationRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService, authenticationManager, jwtTokenProvider))
                .build();

        sampleUserResponse = UserResponseDto.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .createdAt(LocalDateTime.now())
                .build();

        sampleRegistrationRequest = new UserRegistrationDto();
        sampleRegistrationRequest.setUsername("testuser");
        sampleRegistrationRequest.setEmail("test@example.com");
        sampleRegistrationRequest.setPassword("password123");
        sampleRegistrationRequest.setFullName("Test User");
    }

    @Test
    void registerUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(sampleUserResponse);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRegistrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.fullName", is("Test User")));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(sampleUserResponse);

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.fullName", is("Test User")));
    }
}