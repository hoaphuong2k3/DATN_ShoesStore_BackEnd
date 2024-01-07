package com.example.shoestore.core.discount.discountperiodtype.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodRequest;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodSearch;
import com.example.shoestore.core.discount.discountperiodtype.dto.response.DiscountPeriodResponsePrejection;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.entity.DiscountPeriod;
import com.example.shoestore.infrastructure.exception.ValidateException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AdminDiscoutPeriodService {

    ResponseDTO getAllIsActive();

    Page<DiscountPeriodResponsePrejection> search(String code, String name, Integer minPercent, Integer maxPercent, LocalDate fromDate,
                                                  LocalDate toDate, Integer status, Integer isDelete, Integer typePeriod, int page, int size,
                                                  String sortField, String sortOrder) throws ValidateException;

    ResponseDTO createDiscountPeriod(DiscountPeriodRequest createDiscountPeriodDTO);

    ResponseDTO updateDiscountPeriod(DiscountPeriodRequest updateDiscountPeriodDTO);

    ResponseDTO stopDiscountPeriod(Long id);

    ResponseDTO setDiscountPeriodRunNow(Long id);

    ResponseDTO deleteDiscountPeriod(Long id);

    ResponseDTO deleteAll(List<Long> deleteAllDiscountPeriod);
}
