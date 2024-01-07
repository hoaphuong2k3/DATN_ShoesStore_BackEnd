package com.example.shoestore.core.product.admin.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.BrandMapper;
import com.example.shoestore.core.product.admin.dto.request.BrandCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.BrandSearchRequest;
import com.example.shoestore.core.product.admin.dto.request.BrandUpdateRequest;
import com.example.shoestore.core.product.admin.repository.AdminBrandsRepository;
import com.example.shoestore.core.product.admin.repository.AdminShoesRepository;
import com.example.shoestore.core.product.admin.service.BrandService;
import com.example.shoestore.entity.Brand;
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
public class BrandServiceImpl implements BrandService {

    private final AdminBrandsRepository brandRepository;
    private final AdminShoesRepository shoesRepository;
    private final BrandMapper brandMapper;

    @SneakyThrows
    @Override
    public ResponseDTO getALl() {

        List<Brand> brands = brandRepository.getAll();

        if(DataUtils.isEmpty(brands)){
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(brandMapper.entityListToDTOList(brands));

    }

    @Override
    public ResponseDTO create(BrandCreateRequest createRequest) {
        Brand brand = brandMapper.createToEntity(createRequest);

        brand = brandRepository.save(brand);

        return ResponseDTO.success(brandMapper.entityToDTO(brand));
    }

    @Override
    public ResponseDTO update(Long id, BrandUpdateRequest updateRequest) {

        Brand brand = this.getExistById(id);

        brandMapper.updateToEntity(updateRequest, brand);

        brand = brandRepository.save(brand);

        return ResponseDTO.success(brandMapper.entityToDTO(brand));
    }

    @Override
    public ResponseDTO findOne(Long id) {
        return ResponseDTO.success(brandMapper.entityToDTO(this.getExistById(id)));
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteMultipart(List<Long> ids) {

        if (DataUtils.isEmpty(ids)) {
            throw new ValidateException(MessageCode.Commom.NOT_NULL, MessageCode.Brand.ENTITY);
        }

        List<Brand> colors = brandRepository.getExistByIds(ids);

        if (colors.size() != ids.size()) {
            throw new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY);
        }

        List<String> nameByIds = shoesRepository.getNameByBrandIds(ids);

        if (CollectionUtils.isNotEmpty(nameByIds)) {
            String nameError = "";
            if (nameByIds.size() > 1) {
                nameError = String.join(", ", nameByIds);
            }
            throw new ValidateException(MessageCode.Brand.EXIST_DB_OTHER, nameError);
        }

        brandRepository.setIsDelete(ids, IsDeleted.DELETED.getValue());

        return ResponseDTO.success();

    }

    @SneakyThrows
    @Override
    public ResponseDTO search(BrandSearchRequest searchRequest, Pageable pageable) {

        Page<Brand> result = brandRepository.search(searchRequest.getCodeOrName(),pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }

        return ResponseDTO.success(result);
    }

    @SneakyThrows
    private Brand getExistById(Long id) {
        return brandRepository.getExistById(id).orElseThrow(
                () -> new ResourceNotFoundException(MessageCode.Commom.NOT_EXIST, MessageCode.Brand.ENTITY));
    }

}
