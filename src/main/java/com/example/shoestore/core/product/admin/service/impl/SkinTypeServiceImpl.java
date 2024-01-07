package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.SkinTypeMapper;
import com.example.shoestore.core.product.admin.dto.request.*;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.repository.AdminSkinTypeRepository;
import com.example.shoestore.core.product.admin.service.SkinTypeService;
import com.example.shoestore.entity.SkinType;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SkinTypeServiceImpl implements SkinTypeService {

    private final AdminSkinTypeRepository skinTypeRepository;

    private final AdminShoesRepository shoesRepository;

    private final SkinTypeMapper skinTypeMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<SkinType> skinTypes = skinTypeRepository.getAll();

        if(DataUtils.isEmpty(skinTypes)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(skinTypeMapper.entityListToDTOList(skinTypes));
    }

    @Override
    public ResponseDTO create(SkinTypeCreateRequest createRequest) {
        SkinType skinType = skinTypeMapper.createToEntity(createRequest);

        skinType = skinTypeRepository.save(skinType);

        return ResponseDTO.success(skinTypeMapper.entityToDTO(skinType));
    }

    @Override
    public ResponseDTO update(Long id, SkinTypeUpdateRequest updateRequest) {
        SkinType skinType = this.getExistById(id);

        skinTypeMapper.updateToEntity(updateRequest, skinType);

        skinType = skinTypeRepository.save(skinType);

        return ResponseDTO.success(skinTypeMapper.entityToDTO(skinType));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(skinTypeMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.SkinType.ENTITY);
        }

        List<SkinType> colors = skinTypeRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.SkinType.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameBySkinTypeIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.SkinType.EXIST_DB_OTHER, nameError);
        }

        skinTypeRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(SkinTypeSearchRequest searchRequest, Pageable pageable) {
        Page<SkinType> result = skinTypeRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private SkinType getExistById(Long id) {
        return skinTypeRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }

}
