package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.OriginMapper;
import com.example.shoestore.core.product.admin.dto.request.OriginCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.OriginSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.OriginUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminOriginRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.service.OriginService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Origin;
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

public class OriginServiceImpl implements OriginService {

    private final AdminOriginRepository originRepository;

    private final AdminShoesRepository shoesRepository;

    private final OriginMapper originMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Origin> origins = originRepository.getAll();

        if(DataUtils.isEmpty(origins)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(originMapper.entityListToDTOList(origins));
    }

    @Override
    public ResponseDTO create(OriginCreateRequest createRequest) {
        Origin origin = originMapper.createToEntity(createRequest);

        origin = originRepository.save(origin);

        return ResponseDTO.success(originMapper.entityToDTO(origin));
    }

    @Override
    public ResponseDTO update(Long id, OriginUpdateRequest updateRequest) {
        Origin origin = this.getExistById(id);

        originMapper.updateToEntity(updateRequest, origin);

        origin = originRepository.save(origin);

        return ResponseDTO.success(originMapper.entityToDTO(origin));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(originMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Origin.ENTITY);
        }

        List<Origin> origins = originRepository.getExistByIds(ids);

        if (origins.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Origin.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameByOriginIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Origin.EXIST_DB_OTHER, nameError);
        }

        originRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(OriginSearchRequest searchRequest, Pageable pageable) {
        Page<Origin> result = originRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Origin getExistById(Long id) {
        return originRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }

}
