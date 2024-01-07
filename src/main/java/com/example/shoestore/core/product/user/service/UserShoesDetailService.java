package com.example.shoestore.core.product.user.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.user.dto.request.UserShoesDetailRequest;

public interface UserShoesDetailService {

    ResponseDTO getOne(UserShoesDetailRequest detailRequest);

    ResponseDTO getOneByQRCode(String qrCode);

    ResponseDTO findOne(Long id);

}
