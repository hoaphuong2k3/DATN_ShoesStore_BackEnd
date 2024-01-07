package com.example.shoestore.core.product.user.repository.custom;

import com.example.shoestore.core.product.user.dto.request.UserShoesSearchRequest;
import com.example.shoestore.core.product.user.dto.response.UserShoesSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserShoesRepositoryCustom {

    Page<UserShoesSearchResponse> search(UserShoesSearchRequest searchRequest, Pageable pageable);

}
