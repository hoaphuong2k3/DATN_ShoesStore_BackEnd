package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.ImageUploadDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ImageMapper;
import com.example.shoestore.core.product.admin.repository.AdminImageRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesDetailRepositoty;
import com.example.shoestore.core.product.admin.service.ImageService;
import com.example.shoestore.entity.Image;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageUtils imageUtils;

    private final AdminImageRepository imageRepository;

    private final AdminShoesDetailRepositoty shoesDetailRepositoty;

    private final ImageMapper imageMapper;

    @SneakyThrows
    public ResponseDTO downloadImage(Long idShoesDetail) {

        this.getShoesDetailExistById(idShoesDetail);

        List<Image> images = imageRepository.getExistByShoesDetailId(idShoesDetail);

        if (DataUtils.isEmpty(images)) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST,MessageCode.Image.ENTITY);
        }

        return ResponseDTO.success(imageMapper.entityListToDTOList(images));
    }

    @SneakyThrows
    @Override
    public ResponseDTO uploadMultipart(Long shoesDetailId, List<MultipartFile> files) {

        if(files.size()==1){
            if(files.get(0).isEmpty()){
                throw new ValidateException(MessageCode.Commom.FILE_NOT_EMPTY);
            }
        }

        this.getShoesDetailExistById(shoesDetailId);

        List<Image> images = imageMapper.createsToEntities(shoesDetailId,files);
        images = imageRepository.saveAll(images);

        return ResponseDTO.success(imageMapper.entityListToDTOList(images));

    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(imageMapper.entityToDTO(this.getExistById(id)));
    }

    @Override
    public ResponseDTO update(Long id, MultipartFile file) {

        Image image = this.getExistById(id);

        ImageUploadDTO imageUploadDTO = imageUtils.uploadImage(file);

        image.setImgURI(imageUploadDTO.getImgURI());
        image.setImgName(imageUploadDTO.getImgName());

        imageRepository.save(image);

        return ResponseDTO.success(imageMapper.entityToDTO(image));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {

        if (imageRepository.countExistByIds(ids) != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.ImageShoesDetail.ENTITY);
        }

        imageRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }


    @SneakyThrows
    private ShoesDetail getShoesDetailExistById(Long shoesDetailId) {
        return shoesDetailRepositoty.getExistById(shoesDetailId).
                orElseThrow(() -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.ShoesDetail.ENTITY));
    }

    @SneakyThrows
    private Image getExistById(Long id) {
        return imageRepository.getExistById(id).
                orElseThrow(() -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.ImageShoesDetail.ENTITY));
    }
}
