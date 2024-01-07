package com.example.shoestore.core.product.admin.repository.custom;

import com.example.shoestore.core.product.admin.dto.request.ShoesSearchRequest;
import com.example.shoestore.core.product.admin.dto.response.ShoesSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminShoesRepositoryCustom {

    Page<ShoesSearchResponse> search(ShoesSearchRequest searchRequest, Pageable pageable);

}
