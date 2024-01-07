//package com.example.shoestore.core.discount.discountperiod.service;
//
//import com.example.shoestore.core.common.dto.response.ResponseDTO;
//import com.example.shoestore.core.discount.discountperiod.dto.request.DiscountPeriodRequest;
//import com.example.shoestore.entity.DiscountPeriod;
//import com.example.shoestore.infrastructure.exception.ValidateException;
//import org.springframework.data.domain.Page;
//import java.time.LocalDate;
//
//public interface AdminDiscoutPeriodService {
//
//    ResponseDTO getAllIsActive();
//
//    Page<DiscountPeriod> search(String code, String name, Integer minPercent, Integer maxPercent, LocalDate fromDate,
//                                LocalDate toDate, Integer status, Integer is_delete, int page, int size) throws ValidateException;
//
//    ResponseDTO createDiscountPeriod(DiscountPeriodRequest createDiscountPeriodDTO);
//
//    ResponseDTO updateDiscountPeriod(DiscountPeriodRequest updateDiscountPeriodDTO);
//
//    ResponseDTO stopDiscountPeriod(Long id);
//
//    ResponseDTO restoreDiscountPeriod(DiscountPeriodRequest restoreDiscountPeriodDTO);
//
//    ResponseDTO deleteDiscountPeriod(Long id);
//}
