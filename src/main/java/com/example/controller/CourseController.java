package com.example.controller;

import com.example.model.Course;
import com.example.model.Image;
import com.example.model.User;
import com.example.model.Video;
import com.example.payload.CourseRequest;
import com.example.payload.UploadFileResponse;
import com.example.repository.CourseRepository;
import com.example.repository.UserRepository;
import com.example.security.CurrentUser;
import com.example.security.UserPrincipal;
import com.example.service.CourseService;
import com.example.service.ImageService;
import com.example.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private VideoService videoService;

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/{courseId}/image")
    public UploadFileResponse setCourseImage(@RequestParam(name = "file") MultipartFile file,
                                            @PathVariable("courseId") Long courseId) throws IOException {
        Course course = courseRepository.findByCourseId(courseId);

        Image image = imageService.store(file);
        course.setImage(image);
        courseRepository.save(course);

        return new UploadFileResponse(image.getName(), image.getType(), file.getSize());
    }

    @PostMapping("/{courseId}/videos")
    public List<UploadFileResponse> setCourseVideos(@RequestParam(name = "files") MultipartFile[] files,
                                                    @PathVariable("courseId") Long courseId) throws IOException {
        Course course = courseRepository.findByCourseId(courseId);

        Set<Video> videos = new HashSet<>();
        List<UploadFileResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            Video video = videoService.store(file);
            videos.add(video);
            responses.add(new UploadFileResponse(video.getName(), video.getType(), file.getSize()));
        }

        course.setVideos(videos);
        courseRepository.save(course);

        return responses;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCoursesByUserId(@PathVariable("userId") Long userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
         //List<Course> courses = courseService.getCoursesByUser(user);
            Set<Course> courses = user.getCourses();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyCourses(@CurrentUser UserPrincipal currentUser) {
        User user = userRepository.findByUserId(currentUser.getUserId());
        if (user != null) {
           // List<Course> courses = courseService.getCoursesByUser(user);
            Set<Course> courses = user.getCourses();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<Void> addCourse(@Valid @RequestBody CourseRequest request) {
        Course course = new Course(request.getTitle(), request.getDescription());
        boolean flag = courseService.addCourse(course);
        if (!flag)
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/courses/{title}")
                .buildAndExpand(course.getCourseId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping("/{courseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> subscribeCourse(@CurrentUser UserPrincipal currentUser,
                                                @PathVariable("courseId") Long courseId) {
        boolean flag = courseService.subscribeCourse(currentUser, courseId);
        if (flag)
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> unsubscribeCourse(@CurrentUser UserPrincipal currentUser,
                                                  @PathVariable("courseId") Long courseId) {
        boolean flag = courseService.unsubscribeCourse(currentUser, courseId);
        if (flag)
            return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
