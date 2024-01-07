package com.example.shoestore.core.product.admin.repository.custom;

import com.example.shoestore.core.product.admin.dto.request.ShoesDetailSearchRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailReportExcelResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminShoesDetailRepositotyCustom {

    Page<ShoesDetailSearchResponse> search(Long idShoes,ShoesDetailSearchRequest searchRequest, Pageable pageable);

    List<ShoesDetailReportExcelResponse> getAllShoesDetailReport(@Param("status") Integer status);

}
