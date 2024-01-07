package com.example.shoestore.core.statistics.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface StatisticsService {

    ResponseDTO getAllQuantity(LocalDate fromDate,
                               LocalDate toDate);

    ResponseDTO getAllPerWeek();

    ResponseDTO getAllPerWeekBefore();

    ResponseDTO getAllPerMonth();

    ResponseDTO getAllPerMonthBefore();

    ResponseDTO getAllPerYear();

    ResponseDTO getAllPerYearBefore();

    ResponseDTO getAllClientSignUp();

    ResponseDTO getAllTopProduct(LocalDate fromDate, LocalDate toDate);

    ResponseDTO getAllBottomProduct(LocalDate fromDate, LocalDate toDate);

    ResponseDTO getDataPerMonth(Integer month, Integer year);

    ResponseDTO getDataPerYear(Integer year);

    ResponseDTO getDetailShoe(Long shoeId);

    ResponseDTO getTotalPayment(Integer saleStatus, Integer status);

    Page<?> searchInvoice(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate,
                                    LocalDate toDate, Integer status, Integer type, Integer is_delete, int page, int size,
                                    String sortField, String sortOrder);

    Page<?> searchSale(String code, String name, Integer minPrice, Integer maxPrice, LocalDate fromDate,
                          LocalDate toDate, Integer status, Integer type, Integer is_delete, int page, int size,
                          String sortField, String sortOrder);
}
