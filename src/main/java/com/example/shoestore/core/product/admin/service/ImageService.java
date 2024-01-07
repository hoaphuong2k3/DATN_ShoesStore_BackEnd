package com.example.shoestore.core.product.admin.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ResponseDTO downloadImage(Long idShoesDetail);

    ResponseDTO uploadMultipart(Long id, List<MultipartFile> files);

    ResponseDTO findOne(Long id);

    ResponseDTO update(Long id, MultipartFile file);

    ResponseDTO deleteMultipart(List<Long> ids);

}
