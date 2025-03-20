package com.example.blogapi.post.repository;

import com.example.blogapi.post.model.Post;
import com.example.blogapi.config.TestContainerConfig;
import com.example.blogapi.user.model.User;
import com.example.blogapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestContainerConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PostRepositoryIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();

        // Create a test user
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .build();

        userRepository.save(testUser);
    }

    @Test
    void findByAuthorOrderByCreatedAtDesc_ShouldReturnPostsByAuthor() {
        // Create test posts
        Post post1 = Post.builder()
                .title("First Post")
                .content("Content of first post")
                .author(testUser)
                .build();

        Post post2 = Post.builder()
                .title("Second Post")
                .content("Content of second post")
                .author(testUser)
                .build();

        postRepository.saveAll(List.of(post1, post2));

        // Execute test
        List<Post> foundPosts = postRepository.findByAuthorOrderByCreatedAtDesc(testUser);

        // Verify results
        assertThat(foundPosts).hasSize(2);
        assertThat(foundPosts.get(0).getTitle()).isEqualTo("Second Post");
        assertThat(foundPosts.get(1).getTitle()).isEqualTo("First Post");
    }

    @Test
    void findAllByOrderByCreatedAtDesc_ShouldReturnAllPostsOrderByCreatedAtDesc() {
        // Create test posts
        Post post1 = Post.builder()
                .title("First Post")
                .content("Content of first post")
                .author(testUser)
                .build();

        // Add a small delay to ensure different creation timestamps
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Post post2 = Post.builder()
                .title("Second Post")
                .content("Content of second post")
                .author(testUser)
                .build();

        postRepository.saveAll(List.of(post1, post2));

        // Execute test
        List<Post> allPosts = postRepository.findAllByOrderByCreatedAtDesc();

        // Verify results
        assertThat(allPosts).hasSize(2);
        assertThat(allPosts.get(0).getTitle()).isEqualTo("Second Post");
        assertThat(allPosts.get(1).getTitle()).isEqualTo("First Post");
    }
}