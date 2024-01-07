package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.DesignStyleMapper;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.DesignStyleUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminDesignStyleRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.service.DesignStyleService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.DesignStyle;
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
public class DesignStyleServiceImpl implements DesignStyleService {

    private final AdminDesignStyleRepository designStyleRepository;

    private final AdminShoesRepository shoesRepository;

    private final DesignStyleMapper designStyleMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<DesignStyle> designStyles = designStyleRepository.getAll();

        if(DataUtils.isEmpty(designStyles)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(designStyleMapper.entityListToDTOList(designStyles));
    }

    @Override
    public ResponseDTO create(DesignStyleCreateRequest createRequest) {
        DesignStyle designStyle = designStyleMapper.createToEntity(createRequest);

        designStyle = designStyleRepository.save(designStyle);

        return ResponseDTO.success(designStyleMapper.entityToDTO(designStyle));
    }

    @Override
    public ResponseDTO update(Long id, DesignStyleUpdateRequest updateRequest) {
        DesignStyle designStyle = this.getExistById(id);

        designStyleMapper.updateToEntity(updateRequest, designStyle);

        designStyle = designStyleRepository.save(designStyle);

        return ResponseDTO.success(designStyleMapper.entityToDTO(designStyle));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(designStyleMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.DesignStyle.ENTITY);
        }

        List<DesignStyle> designStyles = designStyleRepository.getExistByIds(ids);

        if (designStyles.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.DesignStyle.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameByDesignStyleIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.DesignStyle.EXIST_DB_OTHER, nameError);
        }

        designStyleRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(DesignStyleSearchRequest searchRequest, Pageable pageable) {
        Page<DesignStyle> result = designStyleRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private DesignStyle getExistById(Long id) {
        return designStyleRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }

}
