package com.example.shoestore.core.product.user.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.user.dto.request.UserShoesSearchRequest;
import org.springframework.data.domain.Pageable;

public interface UserShoesService {

    ResponseDTO getOneByShoesId(Long shoesId);

    ResponseDTO search(UserShoesSearchRequest searchRequest, Pageable pageable);

    ResponseDTO getAllColorById(Long id);

    ResponseDTO getAllSizeById(Long id);

    ResponseDTO getOne(Long id);

    ResponseDTO getTop4Sell();

    ResponseDTO getTop4SellLot();

}
