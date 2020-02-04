package com.example.service;

import com.example.model.Image;
import com.example.repository.FileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class ImageService {
    @Autowired
    private FileRepository fileRepository;

    private final static String[] extensions = {"jpeg", "jpg", "img"};

    private final static String STORAGE_PATH = "C:\\Users\\Admin\\IdeaProjects\\repository_test\\src\\main\\resources\\storage\\images";

    public Image store(MultipartFile multipartFile) throws IOException {
        if (!Arrays.asList(extensions).contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename())))
            throw new IOException("Bad extension");

        String fileName = generateFileName(multipartFile.getOriginalFilename());
        String extension = "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        Image image = new Image(fileName + extension, multipartFile.getContentType());

        upload(multipartFile.getBytes(), image.getName());

        return fileRepository.save(image);
    }

    private void upload(byte[] data, String fileName) throws IOException {
        Path path = Paths.get(STORAGE_PATH, fileName);
        Path file = Files.createFile(path);
        try (FileOutputStream outputStream = new FileOutputStream(file.toString())) {
            outputStream.write(data);
        }
    }

    private String generateFileName(String originalName) {
        return DigestUtils.md5Hex(originalName + LocalDateTime.now());
    }

    public Image getFile(Long fileId) {
        return (Image) fileRepository.findByFileId(fileId);
    }
}
