package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodRequest;
import com.example.shoestore.core.discount.discountperiodtype.dto.response.DiscountPeriodResponse;
import com.example.shoestore.entity.DiscountPeriod;

import java.util.List;

public interface DiscountPeriodMapper extends BaseMapper<DiscountPeriod, DiscountPeriodResponse> {

    DiscountPeriod createDTOToEntity(DiscountPeriodRequest createDiscountPeriodDTO);

    DiscountPeriod updateDTOToEntity(DiscountPeriodRequest updateDiscountPeriodDTO);

    List<DiscountPeriodResponse> entityListToDTOList(List<DiscountPeriod> entityList);
}
