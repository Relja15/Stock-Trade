package com.viser.StockTrade.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private static final String UPLOAD_DIR = "D:/New folder/Fax/FileSystem/uploads/";

    public void uploadFile(MultipartFile file, String fileName) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Path filePath = Paths.get(uploadDir.getAbsolutePath(), fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
    }

    public void deleteFile(String path) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, path.split("uploads/")[1]);
        if(Files.exists(filePath)){
            Files.delete(filePath);
        }
    }
}
