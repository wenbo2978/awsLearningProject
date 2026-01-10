package com.aws_practice.services;

import com.aws_practice.security.config.AppAwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.http.MediaType;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final S3Client s3;
    private final AppAwsProperties props;

    public String uploadImage(MultipartFile file, String path) throws IOException{
        if(file.isEmpty())
            throw new IllegalArgumentException("File is empty");
        String contentType = file.getContentType();
        if(contentType == null || !ALLOWED.contains(contentType))
            throw new IllegalArgumentException("Unsupported content type: " + contentType);

        String ext = guessExt(contentType);
        String key = path + "/" + Instant.now().toString().substring(0, 10).replaceAll("-", "/")
                + "/" + UUID.randomUUID() + ext;
        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(props.s3().bucket())
                .key(key)
                .contentType(contentType)
                .metadata(java.util.Map.of("original-filename", safeName(file.getOriginalFilename())))
                .build();
        s3.putObject(req, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    public void deleteImage(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(props.s3().bucket())
                .key(key)
                .build();

        s3.deleteObject(request);
    }

    private static String guessExt(String contentType){
        return switch (contentType){
            case MediaType.IMAGE_JPEG_VALUE -> ".jpg";
            case MediaType.IMAGE_PNG_VALUE -> ".png";
            case "image/webp" -> ".webp";
            case MediaType.IMAGE_GIF_VALUE -> ".gif";
            default -> "";
        };
    }

    private static String safeName(String name){
        if(name == null)
            return "unknown";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
