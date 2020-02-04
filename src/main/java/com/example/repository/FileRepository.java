package com.example.repository;

import com.example.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findByFileId(Long fileId);
}
