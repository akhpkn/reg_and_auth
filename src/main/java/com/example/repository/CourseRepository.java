package com.example.repository;

import com.example.model.Course;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseId(Long courseId);
  //  List<Course> findByCourseIdIn(List<Long> courseIds);
    boolean existsByTitleAndDescription(String title, String description);
    List<Course> findCoursesByUsers(Set<User> users);
}
