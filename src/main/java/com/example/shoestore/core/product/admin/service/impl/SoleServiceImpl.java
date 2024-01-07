package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.SoleMapper;
import com.example.shoestore.core.product.admin.dto.request.SoleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SoleSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.SoleUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.repository.AdminSoleRepository;
import com.example.shoestore.core.product.admin.service.SoleService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Sole;
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

public class SoleServiceImpl implements SoleService {

    private final AdminSoleRepository soleRepository;

    private final AdminShoesRepository shoesRepository;

    private final SoleMapper soleMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Sole> soles = soleRepository.getAll();

        if(DataUtils.isEmpty(soles)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(soleMapper.entityListToDTOList(soles));
    }

    @Override
    public ResponseDTO create(SoleCreateRequest createRequest) {
        Sole sole = soleMapper.createToEntity(createRequest);

        sole = soleRepository.save(sole);

        return ResponseDTO.success(soleMapper.entityToDTO(sole));
    }

    @Override
    public ResponseDTO update(Long id, SoleUpdateRequest updateRequest) {
        Sole sole = this.getExistById(id);

        soleMapper.updateToEntity(updateRequest, sole);

        sole = soleRepository.save(sole);

        return ResponseDTO.success(soleMapper.entityToDTO(sole));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(soleMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Sole.ENTITY);
        }

        List<Sole> colors = soleRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Sole.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameBySoleIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Sole.EXIST_DB_OTHER, nameError);
        }

        soleRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(SoleSearchRequest searchRequest, Pageable pageable) {
        Page<Sole> result = soleRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Sole getExistById(Long id) {
        return soleRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }


}
