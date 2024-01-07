package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ToeMapper;
import com.example.shoestore.core.product.admin.dto.ToeDTO;
import com.example.shoestore.core.product.admin.dto.request.ToeCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.ToeUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.repository.AdminToeRepository;
import com.example.shoestore.core.product.admin.service.ToeService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.SkinType;
import com.example.shoestore.entity.Sole;
import com.example.shoestore.entity.Toe;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ToeServiceImpl implements ToeService {

    private final AdminToeRepository toeRepository;

    private final AdminShoesRepository shoesRepository;

    private final ToeMapper toeMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Toe> toes = toeRepository.getAll();

        if(DataUtils.isEmpty(toes)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(toeMapper.entityListToDTOList(toes));
    }

    @Override
    public ResponseDTO create(ToeCreateRequest createRequest) {
        Toe toe = toeMapper.createToEntity(createRequest);

        toe = toeRepository.save(toe);

        return ResponseDTO.success(toeMapper.entityToDTO(toe));
    }

    @Override
    public ResponseDTO update(Long id, ToeUpdateRequest updateRequest) {
        Toe toe = this.getExistById(id);

        toeMapper.updateToEntity(updateRequest, toe);

        toe = toeRepository.save(toe);

        return ResponseDTO.success(toeMapper.entityToDTO(toe));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(toeMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Toe.ENTITY);
        }

        List<Toe> colors = toeRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Toe.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameByToeIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Toe.EXIST_DB_OTHER, nameError);
        }

        toeRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(ToeSearchRequest searchRequest, Pageable pageable) {
        Page<Toe> result = toeRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Toe getExistById(Long id) {
        return toeRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }

}
