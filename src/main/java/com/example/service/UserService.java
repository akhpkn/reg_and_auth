package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean addUser(User user) {
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail()))
            return false;
        else {
            userRepository.save(user);
            return true;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findByUserId(userId);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
