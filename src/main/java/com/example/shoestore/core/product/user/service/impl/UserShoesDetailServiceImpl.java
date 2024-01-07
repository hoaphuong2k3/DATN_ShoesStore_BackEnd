package com.example.shoestore.core.product.user.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ImageMapper;
import com.example.shoestore.core.product.admin.dto.ImageDTO;
import com.example.shoestore.core.product.user.dto.request.UserShoesDetailRequest;
import com.example.shoestore.core.product.user.dto.response.UserShoesDetailImageResponse;
import com.example.shoestore.core.product.user.dto.response.UserShoesDetailResponse;
import com.example.shoestore.core.product.user.dto.response.UserShoesDetailResponseById;
import com.example.shoestore.core.product.user.repository.UserImageRepository;
import com.example.shoestore.core.product.user.repository.UserShoesDetailRepository;
import com.example.shoestore.core.product.user.service.UserShoesDetailService;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserShoesDetailServiceImpl implements UserShoesDetailService {

    @Qualifier("userShoesDetailRepository")
    private final UserShoesDetailRepository shoesDetailRepository;
    private final UserImageRepository userImageRepository;
    private final ImageMapper imageMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getOne(UserShoesDetailRequest detailRequest) {

        UserShoesDetailResponse detailResponse = shoesDetailRepository.getOne(detailRequest.getShoesId(), detailRequest.getColorId(), detailRequest.getSizeId());

        if(DataUtils.isNull(detailResponse)){
            throw new ValidateException(MessageCode.ShoesDetail.ENTITY_NOT_EXIST);
        }

        List<ImageDTO> images = imageMapper.entityListToDTOList(userImageRepository.getExistByShoesDetailId(detailResponse.getId()));

        UserShoesDetailImageResponse shoesDetailImageResponse = new UserShoesDetailImageResponse();
        shoesDetailImageResponse.setDetailResponse(detailResponse);
        shoesDetailImageResponse.setImages(images);

        return ResponseDTO.success(shoesDetailImageResponse);
    }

    @Override
    public ResponseDTO getOneByQRCode(String qrCode) {

        UserShoesDetailResponse detailResponse = shoesDetailRepository.getOneByQRCode(qrCode);

        List<ImageDTO> images = imageMapper.entityListToDTOList(userImageRepository.getExistByShoesDetailId(detailResponse.getId()));

        UserShoesDetailImageResponse shoesDetailImageResponse = new UserShoesDetailImageResponse();
        shoesDetailImageResponse.setDetailResponse(detailResponse);
        shoesDetailImageResponse.setImages(images);

        return ResponseDTO.success(shoesDetailImageResponse);
    }

    @Override
    public ResponseDTO findOne(Long id) {

        UserShoesDetailResponseById detailResponse = shoesDetailRepository.getOneById(id);

        return ResponseDTO.success(detailResponse);
    }
}
