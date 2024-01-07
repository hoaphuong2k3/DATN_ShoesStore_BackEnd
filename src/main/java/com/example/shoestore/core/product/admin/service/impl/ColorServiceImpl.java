package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ColorMapper;
import com.example.shoestore.core.product.admin.dto.request.ColorCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.ColorSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.ColorUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminColorRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesDetailRepositoty;
import com.example.shoestore.core.product.admin.service.ColorService;
import com.example.shoestore.entity.Color;
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

public class ColorServiceImpl implements ColorService {

    private final AdminColorRepository colorRepository;
    private final AdminShoesDetailRepositoty shoesDetailRepositoty;
    private final ColorMapper colorMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Color> colors = colorRepository.getAll();

        if (DataUtils.isEmpty(colors)) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(colorMapper.entityListToDTOList(colors));
    }

    @SneakyThrows
    @Override
    public ResponseDTO create(ColorCreateRequest createRequest) {

        Color color = colorMapper.createToEntity(createRequest);

        color = colorRepository.save(color);

        return ResponseDTO.success(colorMapper.entityToDTO(color));
    }

    @Override
    public ResponseDTO update(Long id, ColorUpdateRequest updateRequest) {

        Color color = this.getExistById(id);

        colorMapper.updateToEntity(updateRequest, color);

        color = colorRepository.save(color);

        return ResponseDTO.success(colorMapper.entityToDTO(color));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(colorMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {

        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Color.ENTITY);
        }

        List<Color> colors = colorRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Color.ENTITY);
        }

        List<String> nameByIds = shoesDetailRepositoty.getNameByColorIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Color.EXIST_DB_OTHER, nameError);
        }

        colorRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();

    }

    @SneakyThrows
    @Override
    public ResponseDTO search(ColorSearchRequest searchRequest, Pageable pageable) {

        Page<Color> result = colorRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Color getExistById(Long id) {
        return colorRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Color.ENTITY));
    }

}
