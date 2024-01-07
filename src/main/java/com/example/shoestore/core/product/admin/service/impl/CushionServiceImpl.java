package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.CushionMapper;
import com.example.shoestore.core.product.admin.dto.request.CushionCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.CushionSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.CushionUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminCushionRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.service.CushionService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Cushion;
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

public class CushionServiceImpl implements CushionService {

    private final AdminCushionRepository cushionRepository;
    private final AdminShoesRepository shoesRepository;
    private final CushionMapper cushionMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Cushion> cushions = cushionRepository.getAll();

        if(DataUtils.isEmpty(cushions)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(cushionMapper.entityListToDTOList(cushions));
    }

    @Override
    public ResponseDTO create(CushionCreateRequest createRequest) {
        Cushion cushion = cushionMapper.createToEntity(createRequest);

        cushion = cushionRepository.save(cushion);

        return ResponseDTO.success(cushionMapper.entityToDTO(cushion));
    }

    @Override
    public ResponseDTO update(Long id, CushionUpdateRequest updateRequest) {
        Cushion cushion = this.getExistById(id);

        cushionMapper.updateToEntity(updateRequest, cushion);

        cushion = cushionRepository.save(cushion);

        return ResponseDTO.success(cushionMapper.entityToDTO(cushion));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(cushionMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Cushion.ENTITY);
        }

        List<Cushion> colors = cushionRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Cushion.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameByCushionIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Cushion.EXIST_DB_OTHER, nameError);
        }

        cushionRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(CushionSearchRequest searchRequest, Pageable pageable) {
        Page<Cushion> result = cushionRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Cushion getExistById(Long id) {
        return cushionRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }


}
