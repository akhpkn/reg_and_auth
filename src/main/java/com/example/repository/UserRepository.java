package com.example.repository;

import com.example.model.Course;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   /* @Query(value = "SELECT user from User user where user.userId=?1")
    User getUserById(Integer userId); */

   User findByEmail(String email);
   User findByUsernameOrEmail(String username, String email);
   User findByUserId(Long userId);
   User findByUsername(String username);
   Boolean existsByUsername(String username);
   Boolean existsByEmail(String email);
   Boolean existsByUsernameOrEmail(String username, String email);
}
