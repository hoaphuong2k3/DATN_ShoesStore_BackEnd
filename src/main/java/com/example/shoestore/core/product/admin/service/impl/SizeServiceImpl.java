package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.SizeMapper;
import com.example.shoestore.core.product.admin.dto.request.SizeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.SizeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.SizeUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminShoesDetailRepositoty;
import com.example.shoestore.core.product.admin.repository.AdminSizeRepository;
import com.example.shoestore.core.product.admin.service.SizeService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Color;
import com.example.shoestore.entity.Size;
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

public class SizeServiceImpl implements SizeService {

    private final AdminSizeRepository sizeRepository;

    private final AdminShoesDetailRepositoty shoesDetailRepositoty;

    private final SizeMapper sizeMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Size> sizes = sizeRepository.getAll();

        if (DataUtils.isEmpty(sizes)) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(sizeMapper.entityListToDTOList(sizes));
    }

    @Override
    public ResponseDTO create(SizeCreateRequest createRequest) {
        Size size = sizeMapper.createToEntity(createRequest);

        size = sizeRepository.save(size);

        return ResponseDTO.success(sizeMapper.entityToDTO(size));
    }

    @Override
    public ResponseDTO update(Long id, SizeUpdateRequest updateRequest) {
        Size size = this.getExistById(id);

        sizeMapper.updateToEntity(updateRequest, size);

        size = sizeRepository.save(size);

        return ResponseDTO.success(sizeMapper.entityToDTO(size));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(sizeMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Size.ENTITY);
        }

        List<Size> colors = sizeRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Size.ENTITY);
        }

        List<String> nameByIds = shoesDetailRepositoty.getNameBySizeIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Size.EXIST_DB_OTHER, nameError);
        }

        sizeRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(SizeSearchRequest searchRequest, Pageable pageable) {
        Page<Size> result = sizeRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Size getExistById(Long id) {
        return sizeRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Size.ENTITY));
    }

}
