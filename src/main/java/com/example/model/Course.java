package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Table(name = "courses")
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 10000)
    private String description;

    @OneToOne
    @JoinTable(name = "course_image",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Image image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "user_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "course_video", joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<Video> videos = new HashSet<>();

    public Course() {

    }

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Image getImage() {
        return image;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
