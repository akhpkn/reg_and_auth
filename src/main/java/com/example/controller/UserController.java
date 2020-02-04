package com.example.controller;

import com.example.model.Image;
import com.example.model.User;
import com.example.payload.UploadFileResponse;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import com.example.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findByUserId(currentUser.getUserId());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PostMapping("/user/me/edit/name")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeName(@CurrentUser UserPrincipal currentUser,
                                         @RequestParam(name = "name") String name) {
        if (name.length() > 20)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userRepository.findByUserId(currentUser.getUserId());
        user.setName(name);
        userRepository.save(user);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/user/me/edit/username")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeUsername(@CurrentUser UserPrincipal currentUser,
                                            @RequestParam(name = "username") String username) {
        if (username.length() > 15 || username.length() < 4)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (userRepository.existsByUsernameOrEmail(username, username))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userRepository.findByUserId(currentUser.getUserId());
        user.setUsername(username);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("user/me/edit/password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal currentUser,
                                            @RequestParam(name = "password") String password) {
        if (password.length() < 4 || password.length() > 20)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userRepository.findByUserId(currentUser.getUserId());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/me/image")
    @PreAuthorize("hasRole('USER')")
    public UploadFileResponse setUserImage(@CurrentUser UserPrincipal currentUser,
                                       @RequestParam("file") MultipartFile file) throws IOException {
        User user = userRepository.findByUserId(currentUser.getUserId());
        Image image = imageService.store(file);

        user.setImage(image);

        userRepository.save(user);

        return new UploadFileResponse(image.getName(), image.getType(), file.getSize());
    }

    @GetMapping("user/me/imagetype")
    public MediaType getImageContentType(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findByUserId(currentUser.getUserId());
        Image image = user.getImage();
        MediaType mediaType = MediaType.valueOf(image.getType());
        return mediaType;
    }
}
