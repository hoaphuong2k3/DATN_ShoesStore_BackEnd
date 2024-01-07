package com.example.shoestore.core.discount.freegift.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.discount.freegift.model.request.CreateFreeGiftDTO;
import com.example.shoestore.core.discount.freegift.model.request.UpdateFreeGiftDTO;
import com.example.shoestore.core.discount.freegift.model.response.FreeGiftResponse;
import com.example.shoestore.core.discount.freegift.repository.AdminFreeGiftRepository;
import com.example.shoestore.core.discount.freegift.service.FreeGiftService;
import com.example.shoestore.entity.FreeGift;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.ShoesStatus;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FreeGiftServiceImpl implements FreeGiftService {

    @Autowired
    private AdminFreeGiftRepository freeGiftRepository;

    @Override
    public Page<FreeGiftResponse> pageFreeGift(int page, int size) {
        return freeGiftRepository.pageFreeGift(IsDeleted.UNDELETED.getValue(), PageRequest.of(page, size));
    }

    @SneakyThrows
    @Override
    public ResponseDTO create(CreateFreeGiftDTO freeGiftDTO) {
        if (freeGiftDTO == null) {
            return null;
        }
        FreeGift freeGift = new FreeGift();
        freeGift.setCode(GenerateCode.code("FG"));
        freeGift.setName(freeGiftDTO.getName());
        freeGift.setQuantity(freeGiftDTO.getQuantity());
        freeGift.setStatus(ShoesStatus.ON_BUSINESS.getValue());
        freeGift.setIsDeleted(IsDeleted.UNDELETED.getValue());
        FreeGift freeGiftSave = freeGiftRepository.save(freeGift);
        return ResponseDTO.success(freeGiftSave);
    }

    @SneakyThrows
    @Override
    public ResponseDTO update(UpdateFreeGiftDTO freeGiftDTO) {
        if (freeGiftDTO == null || freeGiftDTO.getId() == null) {
            return null;
        }
        FreeGift freeGiftResponse = freeGiftRepository.findById(freeGiftDTO.getId()).get();
        if (freeGiftRepository.existsById(freeGiftDTO.getId())) {
            FreeGift freeGift = new FreeGift();
            freeGift.setId(freeGiftDTO.getId());
            freeGift.setCode(freeGiftResponse.getCode());
            freeGift.setName(freeGiftDTO.getName());
            freeGift.setQuantity(freeGiftDTO.getQuantity());
            freeGift.setStatus(freeGiftResponse.getStatus());
            freeGift.setIsDeleted(freeGiftResponse.getIsDeleted());
            FreeGift freeGiftUpdate = freeGiftRepository.save(freeGift);
            return ResponseDTO.success(freeGiftUpdate);
        }
        return null;
    }


    @Override
    public ResponseDTO delete(Long id) {
        if (freeGiftRepository.existsById(id)) {
            freeGiftRepository.delete(IsDeleted.DELETED.getValue(), id);
            return ResponseDTO.success();
        }
        return null;
    }

    @Override
    public ResponseDTO detail(Long id) {
        return ResponseDTO.success(freeGiftRepository.findById(id).get());
    }

    @SneakyThrows
    @Override
    public ResponseDTO createImage(MultipartFile file, Long id) {
        freeGiftRepository.createImage(file.getBytes(), id);
        return ResponseDTO.success();
    }


}
