package com.aws_practice.controllers;

import com.aws_practice.dto.ImageDto;
import com.aws_practice.models.Image;
import com.aws_practice.response.ApiResponse;
import com.aws_practice.services.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        List<ImageDto> imageDtos = imageService.saveImages(productId, files);
        return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {

        Image image = imageService.getImageById(imageId);
        if(image != null) {
            ImageDto imageDto = imageService.updateImage(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Update success!", imageDto));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        Image image = imageService.getImageById(imageId);
        if(image != null) {
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Delete success!", null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }

}
