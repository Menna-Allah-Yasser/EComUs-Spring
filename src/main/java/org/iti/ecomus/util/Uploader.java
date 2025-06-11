package org.iti.ecomus.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.client.imageStorage.ImageStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Uploader {

    @Autowired
    private ImageStorageClient imageStorageClient;

    private final ExecutorService imageUploadExecutor =
            Executors.newFixedThreadPool(5);

    @Data
    @AllArgsConstructor
    public static class ImageData {
        private String filename;
        private byte[] bytes;
        private long size;
    }

    @Async("imageProcessingTaskExecutor")
    public void batchUploadAsync(String entity, Long productId, List<ImageData> imageDataList) {
        log.info("Starting batch upload of {} images for {}/{}", imageDataList.size(), entity, productId);
        String prefix = entity + "/" + productId.toString() + "/";

        // Process validated images
        List<CompletableFuture<Void>> uploadFutures = imageDataList.stream()
                .map(imageData -> {
                    log.info("Preparing to upload: {} ({}bytes)", imageData.getFilename(), imageData.getBytes().length);
                    
                    return CompletableFuture.runAsync(() -> {
                        try (InputStream is = new ByteArrayInputStream(imageData.getBytes())) {
                            String result = imageStorageClient.uploadImage(
                                    prefix + imageData.getFilename(),
                                    is,
                                    imageData.getSize()
                            );
                            log.info("Upload result for {}: {}", imageData.getFilename(), result);
                            if (result == null || result.isEmpty()) {
                                log.error("Upload failed for {}: empty result returned", imageData.getFilename());
                            } else {
                                log.info("Successfully uploaded: {}", imageData.getFilename());
                            }
                        } catch (IOException e) {
                            log.error("Failed to upload image: {} - {}", imageData.getFilename(), e.getMessage(), e);
                        }
                    }, imageUploadExecutor);
                })
                .collect(Collectors.toList());

        // Wait for all uploads in background
        int successCount = 0;
        for (CompletableFuture<Void> future : uploadFutures) {
            try {
                future.get(30, TimeUnit.SECONDS);
                successCount++;
            } catch (Exception e) {
                log.error("Background upload failed: {}", e.getMessage(), e);
            }
        }

        log.info("Batch upload completed: {}/{} images succeeded for {}/{}", 
                 successCount, imageDataList.size(), entity, productId);
    }

    // Keep the old method for backward compatibility
    @Async("imageProcessingTaskExecutor")
    public void batchUploadAsync(String entity, Long productId, MultipartFile[] images) {
        log.info("Starting batch upload of {} images for {}/{}", images.length, entity, productId);
        
        // Create a list to store validated images and their data
        List<ImageData> validatedImages = new ArrayList<>();

        // First, validate all images and read their bytes
        for (int i = 0; i < images.length; i++) {
            MultipartFile image = images[i];
            if (image == null) {
                log.error("Image at index {} is null", i);
                continue;
            }
            
            if (image.isEmpty() || image.getSize() == 0) {
                log.error("Image at index {} is empty or has zero size: {}", i, image.getOriginalFilename());
                continue;
            }
            
            try {
                byte[] bytes = image.getBytes();
                if (bytes.length == 0) {
                    log.error("Image at index {} has empty byte array: {}", i, image.getOriginalFilename());
                    continue;
                }
                
                log.info("Image {} validated: {} ({}bytes)", i, image.getOriginalFilename(), bytes.length);
                validatedImages.add(new ImageData(image.getOriginalFilename(), bytes, image.getSize()));
            } catch (IOException e) {
                log.error("Failed to read image at index {}: {} - {}", i, image.getOriginalFilename(), e.getMessage(), e);
            }
        }
        
        // Use the new method to process the validated images
        batchUploadAsync(entity, productId, validatedImages);
    }
}
