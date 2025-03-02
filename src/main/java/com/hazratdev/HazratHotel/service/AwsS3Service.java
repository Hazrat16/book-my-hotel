package com.hazratdev.HazratHotel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class AwsS3Service {

    @Value("${file.storage.location}")
    private String storageDirectory;

    public String saveImageToS3(MultipartFile photo) {
        File directory = new File(storageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path filePath = Path.of(storageDirectory, fileName);

            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toAbsolutePath().toString();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to save image to local storage: " + e.getMessage());
        }
    }
}
