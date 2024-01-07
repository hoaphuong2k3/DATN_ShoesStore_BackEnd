package com.example.shoestore.core.discount.invoiceproduct.voucher.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountProjection;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountResponse;
import com.example.shoestore.infrastructure.exception.ValidateException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AdminVoucherService {

    Page<DiscountResponse> getAll(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate,
                                    LocalDate toDate, Integer status, Integer type, Integer is_delete, int page, int size,
                                    String sortField, String sortOrder);

    ResponseDTO getAllIsActive(Long id);

    ResponseDTO createVoucher(DiscountRequest createVoucherDTO);

    ResponseDTO setVoucherRunNow(Long id) throws ValidateException;

    ResponseDTO updateVoucher(DiscountRequest updateVoucherDTO);

    ResponseDTO stopVoucher(Long id);

    ResponseDTO restoreVoucher(DiscountRequest restoreVoucherDTO);

    ResponseDTO deleteVoucher(Long id);

    ResponseDTO deleteAllVoucher(List<Long> deleteAllVoucher);
}
