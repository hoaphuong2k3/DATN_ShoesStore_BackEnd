package com.example.shoestore.core.discount.freegift.service;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.discount.freegift.model.request.CreateFreeGiftDTO;
import com.example.shoestore.core.discount.freegift.model.request.UpdateFreeGiftDTO;
import com.example.shoestore.core.discount.freegift.model.response.FreeGiftResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface FreeGiftService {

    Page<FreeGiftResponse> pageFreeGift(int page, int size);

    ResponseDTO create(CreateFreeGiftDTO feeGiftDTO);

    ResponseDTO update(UpdateFreeGiftDTO feeGiftDTO);

    ResponseDTO delete(Long id);

    ResponseDTO detail(Long id);

    ResponseDTO createImage(MultipartFile file, Long id);

}
