package com.example.blogapi.user.repository;

import com.example.blogapi.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<User> findByUsernameCustom(@Param("username") String username);

    @Query(value = "SELECT u.* FROM users u JOIN posts p ON u.id = p.user_id " +
            "WHERE p.is_published = true GROUP BY u.id HAVING COUNT(p.id) > :postCount",
            nativeQuery = true)
    List<User> findActiveAuthorsWithMoreThanXPosts(@Param("postCount") int postCount);
}