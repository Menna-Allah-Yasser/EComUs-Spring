package org.iti.ecomus.client.imageStorage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.BlobStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AzureImageStorageClient implements ImageStorageClient{

    private final BlobServiceClient blobServiceClient;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String container;

    public AzureImageStorageClient(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    @Override
    public String uploadImage( String originalImageName, InputStream data, long length) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(container);

            if (!blobContainerClient.exists()) {
                blobContainerClient.create();
            }
//            String originalImageName1 =  UUID.randomUUID().toString() + originalImageName.substring(originalImageName.lastIndexOf("."));
            BlobClient blobClient = blobContainerClient.getBlobClient(originalImageName);
//            blobClient.upload(data, length, true);
            BlobHttpHeaders headers = new BlobHttpHeaders();
            String contentType = getContentType(originalImageName);
            headers.setContentType(contentType);

            BlobRequestConditions requestConditions = new BlobRequestConditions();

            blobClient.uploadWithResponse(
                    data,
                    length,
                    null,
                    headers,
                    null,
                    null,
                    requestConditions,
                    null,
                    null);

            return blobClient.getBlobUrl();
        } catch (BlobStorageException e) {
            log.error("Failed to upload to Azure Blob Storage: " + e.getMessage(), e);
        }catch (Exception e) {
            log.error("Unexpected error during image upload: " + e.getMessage(), e);
        }
            return "";
    }

    @Override
    public List<String> getAllImages(String folder, String id) {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(container);
        return blobContainerClient.listBlobsByHierarchy(folder + "/" + id + "/")
                .stream()
                .map(blobItem -> blobItem.getName())
                .toList();
    }


    @Override
    public boolean deleteImage(String imageName) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(container);
            BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

            if (blobClient.exists()) {
                blobClient.delete();
                log.info("Successfully deleted image: {}", imageName);
                return true;
            } else {
                log.warn("Attempted to delete non-existing image: {}", imageName);
                return false;
            }
        } catch (BlobStorageException e) {
            log.error("Failed to delete image from Azure Blob Storage: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during image deletion: " + e.getMessage(), e);
        }
        return false;
    }

    private String getContentType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

}
