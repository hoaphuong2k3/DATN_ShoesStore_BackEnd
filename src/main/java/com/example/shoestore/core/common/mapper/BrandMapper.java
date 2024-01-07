package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.BrandDTO;
import com.example.shoestore.core.product.admin.dto.request.BrandCreateRequest;
import com.example.shoestore.core.product.admin.dto.request.BrandUpdateRequest;
import com.example.shoestore.entity.Brand;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand, BrandDTO> {

    List<BrandDTO> entityListToDTOList(List<Brand> entityList);

    Brand createToEntity(BrandCreateRequest createRequest);

    void updateToEntity(BrandUpdateRequest updateRequest, Brand entity);

}
