package org.iti.ecomus.client.imageStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@Slf4j
public class LocalImageStorageClient implements ImageStorageClient {

    @Value("${local.image.storage.path:uploads}")
    private String rootDir;
    
    @Value("${local.image.base.url:/images}")
    private String baseUrl;

    @Override
    public String uploadImage(String originalImageName, InputStream data, long length) throws IOException {
        String filePath = Paths.get(rootDir, originalImageName).toString();
        File file = new File(filePath);

        // Create parent directories if they don't exist
        file.getParentFile().mkdirs();

        try (OutputStream out = new FileOutputStream(file)) {
            data.transferTo(out);
        }

        // Return a web-accessible URL instead of file path
        return baseUrl + "/" + originalImageName;
    }

    @Override
    public List<String> getAllImages(String folder, String id) {
        List<String> imageList = new ArrayList<>();
        Path dirPath = Paths.get(rootDir, folder, id);

        if (!Files.exists(dirPath)) return imageList;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    // Return web URLs instead of file paths
                    String relativePath = folder + "/" + id + "/" + entry.getFileName().toString();
                    imageList.add(baseUrl + "/" + relativePath);
                }
            }
        } catch (IOException e) {
            log.error("Error reading local image directory: " + e.getMessage(), e);
        }

        return imageList;
    }

    @Override
    public boolean deleteImage(String imageName) {
        Path path = Paths.get(rootDir, imageName);
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Failed to delete local image: " + imageName, e);
            return false;
        }
    }
}