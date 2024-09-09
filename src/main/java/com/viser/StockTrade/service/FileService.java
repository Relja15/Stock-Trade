package com.viser.StockTrade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${myapp.custom.upload-dir}")
    private String UPLOAD_DIR;

    /**
     * Uploads a file to the specified directory with the given file name.
     *
     * This method saves the given file to the directory specified by `UPLOAD_DIR`. If the directory does not
     * exist, it is created. The file is written to the path composed of `UPLOAD_DIR` and the specified file name.
     *
     * @param file the file to be uploaded (a {@link MultipartFile})
     * @param fileName the name to be used for the uploaded file (a {@link String})
     * @throws IOException if an I/O error occurs while creating the directory or writing the file
     */
    public void uploadFile(MultipartFile file, String fileName) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Path filePath = Paths.get(uploadDir.getAbsolutePath(), fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
    }

    /**
     * Deletes a file located at the specified path.
     *
     * This method deletes a file from the directory specified by `UPLOAD_DIR`. The file is identified using
     * the provided path, which is assumed to be a relative path starting with "uploads/". The method extracts
     * the file name from the provided path and deletes the corresponding file if it exists.
     *
     * @param path the relative path to the file to be deleted, starting with "uploads/" (a {@link String})
     * @throws IOException if an I/O error occurs while deleting the file
     */
    public void deleteFile(String path) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR, path.split("uploads/")[1]);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
