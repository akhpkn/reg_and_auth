package com.example.service;

import com.example.model.Course;
import com.example.model.User;
import com.example.repository.CourseRepository;
import com.example.repository.UserRepository;
import com.example.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByUser(User user) {
        Set<User> users = new HashSet<>();
        users.add(user);
        return courseRepository.findCoursesByUsers(users);
    }

    public boolean addCourse(Course course) {
        if (!courseRepository.existsByTitleAndDescription(course.getTitle(), course.getDescription())) {
            courseRepository.save(course);
            return true;
        }
        return false;
    }

    public boolean subscribeCourse(UserPrincipal currentUser, Long courseId) {
        if (currentUser != null) {
            User user = userRepository.findByUserId(currentUser.getUserId());
            if (courseRepository.findByCourseId(courseId) != null) {
                user.subscribe(courseRepository.findByCourseId(courseId));
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean unsubscribeCourse(UserPrincipal currentUser, Long courseId) {
        if (currentUser != null) {
            User user = userRepository.findByUserId(currentUser.getUserId());
            if (courseRepository.findByCourseId(courseId) != null) {
                user.unsubscribe(courseRepository.findByCourseId(courseId));
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }
}
