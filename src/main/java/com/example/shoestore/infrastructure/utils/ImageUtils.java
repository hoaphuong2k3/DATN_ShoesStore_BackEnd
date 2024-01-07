package com.example.shoestore.infrastructure.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.shoestore.core.common.dto.ImageUploadDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ImageUtils {

    @Value("${aws.s3.bucketName}")
    private String bucketName;
    private final AmazonS3 amazonS3;

    public ImageUtils() {
        this.amazonS3 = AmazonS3ClientBuilder.defaultClient();
    }

    @SneakyThrows
    public ImageUploadDTO uploadImage(MultipartFile file) {

        long size = file.getSize();

        String fileName = file.getOriginalFilename();

        String uri = this.generateUniqueFileName(fileName);

        InputStream inputStream = file.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);

        amazonS3.putObject(bucketName, uri, inputStream, metadata);
        amazonS3.setObjectAcl(bucketName, uri, CannedAccessControlList.PublicRead);

        ImageUploadDTO.ImageUploadDTOBuilder dto = ImageUploadDTO.builder();

        dto.imgURI(uri);
        dto.imgName(fileName);

        return dto.build();
    }

    public static String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID() + "-" + originalFileName;
    }

    @SneakyThrows
    public ImageUploadDTO uploadImageCustom(MultipartFile file, Map<String, String> mapURI) {
        long size = file.getSize();
        String fileName = file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);

        String uri = mapURI.getOrDefault(fileName, "");

        amazonS3.putObject(bucketName, uri, inputStream, metadata);
        amazonS3.setObjectAcl(bucketName, uri, CannedAccessControlList.PublicRead);

        return ImageUploadDTO.builder()
                .imgURI(uri)
                .imgName(fileName)
                .build();
    }

    @SneakyThrows
    public List<ImageUploadDTO> uploadImagesCustom(List<MultipartFile> files, Map<String, String> mapURI) {
        return files.stream()
                .map(file -> uploadImageCustom(file, mapURI))
                .collect(Collectors.toList());
    }

}
