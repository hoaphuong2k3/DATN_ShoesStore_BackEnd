package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.dto.ImageUploadDTO;
import com.example.shoestore.core.common.mapper.ImageMapper;
import com.example.shoestore.core.product.admin.dto.ImageDTO;
import com.example.shoestore.entity.Image;
import com.example.shoestore.infrastructure.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageMapperImpl implements ImageMapper {

    private final ImageUtils imageUtils;

    @Override
    public Image DTOToEntity(ImageDTO dto) {

        return null;
    }

    @Override
    public ImageDTO entityToDTO(Image entity) {

        ImageDTO dto = new ImageDTO();
        dto.setId(entity.getId());
        dto.setShoesDetailId(entity.getShoesDetailId());
        dto.setImgName(entity.getImgName());
        dto.setImgURI(entity.getImgURI());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedTime(entity.getUpdatedTime());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    @Override
    public List<ImageDTO> entityListToDTOList(List<Image> entityList) {

        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<ImageDTO> dtoList = new ArrayList<>();

        entityList.forEach(entity -> {
            ImageDTO dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public List<Image> createsToEntities(Long shoesDetailId, List<MultipartFile> files) {

        List<Image> images = new ArrayList<>();

        files.forEach(file -> {
            Image.ImageBuilder imageBuilder = Image.builder();
            ImageUploadDTO imageUploadDTO = imageUtils.uploadImage(file);
            imageBuilder.imgURI(imageUploadDTO.getImgURI());
            imageBuilder.imgName(imageUploadDTO.getImgName());
            images.add(imageBuilder.build());
        });

        images.forEach(entity -> {

            entity.setShoesDetailId(shoesDetailId);
            entity.setIsDeleted(Boolean.FALSE);

        });

        return images;
    }

}
