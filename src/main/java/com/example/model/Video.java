package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "videos")
public class Video extends File {

    public Video() {
        super();
    }

    public Video(String name, String type) {
        super(name, type);
    }
}
