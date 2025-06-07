package org.iti.ecomus.client.imageStorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImageStorageClient {
    String uploadImage( String originalImageName, InputStream data, long length) throws IOException;

    List<String> getAllImages(String folder, String id);

    boolean deleteImage(String imageName);

}
