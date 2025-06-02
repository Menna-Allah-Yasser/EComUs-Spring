package org.iti.ecomus.util;

import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.client.imageStorage.ImageStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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

    @Async("imageProcessingTaskExecutor")
    public void batchUploadAsync(String entity,Long productId, MultipartFile[] images) {
        String prefix = entity+ "/" + productId.toString() + "/";

        List<CompletableFuture<Void>> uploadFutures = Arrays.stream(images)
                .filter(image -> !image.isEmpty())
                .map(image -> CompletableFuture.runAsync(() -> {
                    try (InputStream is = image.getInputStream()) {
                        imageStorageClient.uploadImage(
                                prefix + image.getOriginalFilename(),
                                is,
                                image.getSize()
                        );
                        log.info("Successfully uploaded: {}", image.getOriginalFilename());
                    } catch (IOException e) {
                        log.error("Failed to upload image: {}", image.getOriginalFilename(), e);
                        // Don't throw exception in background task
                    }
                }, imageUploadExecutor))
                .collect(Collectors.toList());

        // Wait for all uploads in background
        uploadFutures.forEach(future -> {
            try {
                future.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("Background upload failed", e);
            }
        });

        log.info("All image uploads completed for product: {}", productId);
    }
}
