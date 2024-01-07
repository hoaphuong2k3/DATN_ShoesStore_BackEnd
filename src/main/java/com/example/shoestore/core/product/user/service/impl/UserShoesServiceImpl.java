package com.example.shoestore.core.product.user.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.product.user.dto.request.UserShoesSearchRequest;
import com.example.shoestore.core.product.user.dto.response.*;
import com.example.shoestore.core.product.user.repository.UserColorRepository;
import com.example.shoestore.core.product.user.repository.UserShoesRepository;
import com.example.shoestore.core.product.user.repository.UserSizeRepository;
import com.example.shoestore.core.product.user.service.UserShoesService;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserShoesServiceImpl implements UserShoesService {

    private final UserShoesRepository shoesRepository;
    private final UserSizeRepository sizeRepository;
    private final UserColorRepository colorRepository;

    @SneakyThrows
    @Override
    public ResponseDTO search(UserShoesSearchRequest searchRequest, Pageable pageable) {
        Page<UserShoesSearchResponse> result = shoesRepository.search(searchRequest, pageable);

        if (result.isEmpty()) {
            throw new ValidateException(MessageCode.Commom.RESULT_EMPTY);
        }
        return ResponseDTO.success(result);
    }

    @Override
    public ResponseDTO getAllColorById(Long id) {

        List<UserColorResponse> colorResponses = colorRepository.getAllByShoesId(id);

        return ResponseDTO.success(colorResponses);
    }

    @Override
    public ResponseDTO getAllSizeById(Long id) {

        List<UserSizeResponse> colorResponses = sizeRepository.getAllByShoesId(id);

        return ResponseDTO.success(colorResponses);
    }

    @Override
    public ResponseDTO getOne(Long id) {

        UserShoesSearchByIdResponse searchByIdResponse = shoesRepository.getOneCustom(id);

        return ResponseDTO.success(searchByIdResponse);
    }

    @Override
    public ResponseDTO getOneByShoesId(Long shoesId) {

        UserShoesDetailResponse searchByIdResponse = shoesRepository.getShoesDetailIndexOneByShoesId(shoesId);

        return ResponseDTO.success(searchByIdResponse);
    }

    @Override
    public ResponseDTO getTop4Sell() {
        return ResponseDTO.success(shoesRepository.getTop4Sell());
    }

    @Override
    public ResponseDTO getTop4SellLot() {
        return ResponseDTO.success(shoesRepository.getTop4SellLot());
    }


}
