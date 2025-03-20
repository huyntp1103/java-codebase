package com.example.blogapi.user.repository;

import com.example.blogapi.config.TestContainerConfig;
import com.example.blogapi.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void findByUsername_ShouldReturnUserWithMatchingUsername() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmail_ShouldReturnUserWithMatchingEmail() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void existsByUsername_ShouldReturnTrueIfUsernameExists() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();
        userRepository.save(user);

        // Act & Assert
        assertThat(userRepository.existsByUsername("testuser")).isTrue();
        assertThat(userRepository.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    void existsByEmail_ShouldReturnTrueIfEmailExists() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();
        userRepository.save(user);

        // Act & Assert
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    void findById_ShouldReturnUserWithMatchingId() {
        // Arrange
        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();
        User savedUser = userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }
}