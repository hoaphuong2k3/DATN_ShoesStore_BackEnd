package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileValidateUtils {

    @Value("${image.maxSize}")
    private long imageMaxSize;

    @Value("${image.allowedFormats}")
    private String allowedImageFormats;

    @Value("${image.maxLength}")
    private long imageMaxlength;

    @Value("${excel.maxSize}")
    private long excelMaxSize;

    @Value("${excel.allowedFormat}")
    private String allowedExcelFormat;

    @Value("${excel.maxLength}")
    private long excelMaxlength;

    @SneakyThrows
    public void validateExcelFile(MultipartFile file) {
        if (file.isEmpty()) {
          throw new ValidateException(MessageCode.Commom.FILE_NOT_EMPTY);
        }

        if(file.getOriginalFilename().length()>excelMaxlength){
            throw new ValidateException(MessageCode.Commom.FILE_MAX_LENGTH,String.valueOf(excelMaxlength));
        }

        long sizeInKB = (file.getSize() + 1023) / 1024;
        if (sizeInKB > excelMaxSize) {
            throw new ValidateException(MessageCode.Commom.FILE_MAX_SIZE,String.valueOf(excelMaxSize));
        }

        String originalFileName = file.getOriginalFilename().toLowerCase();
        if (DataUtils.isNotNull(originalFileName) && !originalFileName.endsWith("." + allowedExcelFormat)) {
            throw new ValidateException(MessageCode.Commom.FILE_ALLOWED,allowedExcelFormat);
        }
    }

    @SneakyThrows
    public void validateImageFile(MultipartFile file) {

        if (file.isEmpty()||DataUtils.isNull(file)) {
            throw new ValidateException(MessageCode.Commom.FILE_NOT_EMPTY);
        }

        if(file.getOriginalFilename().length()>imageMaxlength){
            throw new ValidateException(MessageCode.Commom.FILE_MAX_LENGTH,String.valueOf(imageMaxlength));
        }

        long sizeInKB = (file.getSize() + 1023) / 1024;
        if (sizeInKB > imageMaxSize) {
            throw new ValidateException(MessageCode.Commom.FILE_MAX_SIZE,String.valueOf(imageMaxSize));
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (DataUtils.isNotNull(fileExtension) && !allowedImageFormats.contains(fileExtension.toLowerCase())) {
            throw new ValidateException(MessageCode.Commom.FILE_ALLOWED, allowedImageFormats);
        }

    }

    @SneakyThrows
    public void validateImageFiles(List<MultipartFile> files) {

        if(DataUtils.isNull(files)){
            throw new ValidateException(MessageCode.Commom.FILE_NOT_EMPTY);
        }

        for (MultipartFile file : files) {
            validateImageFile(file);
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex >= 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }


}
