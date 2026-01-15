package com.aws_practice.services.image;

import com.aws_practice.dto.ImageDto;
import com.aws_practice.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService{
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    ImageDto updateImage(MultipartFile file,  Long imageId);
}
