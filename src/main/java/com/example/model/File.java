package com.example.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String name;

    private String type;

    public File() {

    }

    public File(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
