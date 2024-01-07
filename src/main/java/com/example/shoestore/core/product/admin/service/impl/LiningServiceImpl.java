package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.LiningMapper;
import com.example.shoestore.core.product.admin.dto.request.LiningCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.LiningSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.LiningUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminLiningRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.service.LiningService;
import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Lining;
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
public class LiningServiceImpl implements LiningService {

    private final AdminLiningRepository liningRepository;

    private final AdminShoesRepository shoesRepository;

    private final LiningMapper liningMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Lining> linings = liningRepository.getAll();

        if(DataUtils.isEmpty(linings)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(liningMapper.entityListToDTOList(linings));
    }

    @Override
    public ResponseDTO create(LiningCreateRequest createRequest) {
        Lining lining = liningMapper.createToEntity(createRequest);

        lining = liningRepository.save(lining);

        return ResponseDTO.success(liningMapper.entityToDTO(lining));
    }

    @Override
    public ResponseDTO update(Long id, LiningUpdateRequest updateRequest) {
        Lining lining = this.getExistById(id);

        liningMapper.updateToEntity(updateRequest, lining);

        lining = liningRepository.save(lining);

        return ResponseDTO.success(liningMapper.entityToDTO(lining));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(liningMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {
        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Lining.ENTITY);
        }

        List<Lining> colors = liningRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Lining.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameByLiningIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Lining.EXIST_DB_OTHER, nameError);
        }

        liningRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO search(LiningSearchRequest searchRequest, Pageable pageable) {
        Page<Lining> result = liningRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Lining getExistById(Long id) {
        return liningRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }


}
