package com.example.shoestore.core.discount.invoiceproduct.promo.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.PromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.UpdatePromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountProjection;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AdminPromosService {

    Page<DiscountResponse> search(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate,
                                    LocalDate toDate, Integer status, Integer type, Integer is_delete, int page, int size,
                                    String sortField, String sortOrder);

    ResponseDTO getAll();

    ResponseDTO getAllIsActive();

    ResponseDTO createPromos(PromoRequest createPromoDTO);

    ResponseDTO updatePromos(PromoRequest updatePromoDTO);

    ResponseDTO detailPromos(Long id);

    ResponseDTO setPromoRunNow(Long id);

    ResponseDTO stopPromos(Long id);

    ResponseDTO restorePromos(DiscountRequest restorePromoDTO);

    ResponseDTO deletePromos(Long promoId);

    ResponseDTO deleteAllPromo(List<Long> deleteAllPromo);
}
