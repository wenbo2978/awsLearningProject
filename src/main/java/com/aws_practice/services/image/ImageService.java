package com.aws_practice.services.image;

import com.aws_practice.dto.ImageDto;
import com.aws_practice.exceptions.ResourceNotFoundException;
import com.aws_practice.models.Image;
import com.aws_practice.models.Product;
import com.aws_practice.repositories.ImageRepository;
import com.aws_practice.services.S3StorageService;
import com.aws_practice.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final S3StorageService s3StorageService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(image -> {
            String key = image.getDownloadUrl();
            s3StorageService.deleteImage(key);
            imageRepository.delete(image);
        }, () -> {
            throw new ResourceNotFoundException("No image found with id: " + id);
        });
    }

    @Override
    public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setProduct(product);
                String key = s3StorageService.uploadImage(file, "product");
                image.setDownloadUrl(key);
                Image savedImage = imageRepository.save(image);
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch(IOException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public ImageDto updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            String key = s3StorageService.updateImage(file, image.getDownloadUrl());
            Image savedImage = imageRepository.save(image);
            ImageDto imageDto = new ImageDto();
            imageDto.setId(savedImage.getId());
            imageDto.setFileName(savedImage.getFileName());
            imageDto.setDownloadUrl(savedImage.getDownloadUrl());
            return imageDto;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
