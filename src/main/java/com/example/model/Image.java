package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Image extends File {

    public Image() {
        super();
    }

    public Image(String name, String type) {
        super(name, type);
    }
}
