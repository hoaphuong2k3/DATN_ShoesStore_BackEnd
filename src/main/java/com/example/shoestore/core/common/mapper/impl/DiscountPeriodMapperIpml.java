package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.DiscountPeriodMapper;
import com.example.shoestore.core.discount.discountperiodtype.dto.request.DiscountPeriodRequest;
import com.example.shoestore.core.discount.discountperiodtype.dto.response.DiscountPeriodResponse;
import com.example.shoestore.entity.DiscountPeriod;
import com.example.shoestore.infrastructure.constants.DatePattern;
import com.example.shoestore.infrastructure.constants.DiscountStatus;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountPeriodMapperIpml implements DiscountPeriodMapper {

    @Override
    public DiscountPeriod createDTOToEntity(DiscountPeriodRequest createDiscountPeriodDTO) {
        if(createDiscountPeriodDTO == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        LocalDate startDate = LocalDate.parse(createDiscountPeriodDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createDiscountPeriodDTO.getEndDate(), formatter);

        DiscountPeriod discountPeriod = new DiscountPeriod();
        discountPeriod.setName(createDiscountPeriodDTO.getName());
        discountPeriod.setCode(GenerateCode.code("DP"));
        if (createDiscountPeriodDTO.getTypePeriod() == 0){
            this.validateSalePercent(createDiscountPeriodDTO);
            discountPeriod.setSalePercent(Integer.valueOf(createDiscountPeriodDTO.getSalePercent()));
            discountPeriod.setMinPrice(BigDecimal.valueOf(Long.parseLong(createDiscountPeriodDTO.getMinPrice())));
            discountPeriod.setTypePeriod(0);
        } else {
            discountPeriod.setTypePeriod(1);
            discountPeriod.setSalePercent(-1);
            discountPeriod.setMinPrice(null);
        }
        if (startDate.isEqual(LocalDate.now())){
            discountPeriod.setStatus(DiscountStatus.RUNNING.getValue());
        } else {
            discountPeriod.setStatus(DiscountStatus.WAITING.getValue());
        }
        discountPeriod.setCreatedBy(createDiscountPeriodDTO.getCreatedBy());
        discountPeriod.setUpdatedBy(createDiscountPeriodDTO.getUpdatedBy());
        discountPeriod.setStartDate(startDate);
        discountPeriod.setEndDate(endDate);
        discountPeriod.setGiftId(createDiscountPeriodDTO.getGiftId());

        return discountPeriod;
    }

    @Override
    public DiscountPeriod updateDTOToEntity(DiscountPeriodRequest updateDiscountPeriodDTO) {
        if(updateDiscountPeriodDTO == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        LocalDate startDate = LocalDate.parse(updateDiscountPeriodDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(updateDiscountPeriodDTO.getEndDate(), formatter);

        DiscountPeriod discountPeriod = new DiscountPeriod();
        discountPeriod.setId(updateDiscountPeriodDTO.getId());
        discountPeriod.setName(updateDiscountPeriodDTO.getName());
        discountPeriod.setCode(updateDiscountPeriodDTO.getCode());
        if (startDate.isEqual(LocalDate.now())){
            discountPeriod.setStatus(DiscountStatus.RUNNING.getValue());
        } else {
            discountPeriod.setStatus(DiscountStatus.WAITING.getValue());
        }
        if (updateDiscountPeriodDTO.getTypePeriod() == 0){
            discountPeriod.setSalePercent(Integer.valueOf(updateDiscountPeriodDTO.getSalePercent()));
            discountPeriod.setGiftId(updateDiscountPeriodDTO.getGiftId());
            discountPeriod.setMinPrice(BigDecimal.valueOf(Long.parseLong(updateDiscountPeriodDTO.getMinPrice())));
        } else {
            discountPeriod.setSalePercent(-1);
        }
        discountPeriod.setIsDeleted(updateDiscountPeriodDTO.getIsDeleted());
        discountPeriod.setCreatedBy(updateDiscountPeriodDTO.getCreatedBy());
        discountPeriod.setUpdatedBy(updateDiscountPeriodDTO.getUpdatedBy());
        discountPeriod.setStartDate(startDate);
        discountPeriod.setEndDate(endDate);
        discountPeriod.setTypePeriod(updateDiscountPeriodDTO.getTypePeriod());

        return discountPeriod;
    }

    @Override
    public DiscountPeriod DTOToEntity(DiscountPeriodResponse dto) {
        return null;
    }

    @Override
    public List<DiscountPeriodResponse> entityListToDTOList(List<DiscountPeriod> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<DiscountPeriodResponse> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            DiscountPeriodResponse dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public DiscountPeriodResponse entityToDTO(DiscountPeriod entity) {
        if (entity == null) {
            return null;
        }

        DiscountPeriodResponse discountPeriodResponse = new DiscountPeriodResponse();
        discountPeriodResponse.setId(entity.getId());
        discountPeriodResponse.setName(entity.getName());
        discountPeriodResponse.setCode(entity.getCode());
        discountPeriodResponse.setSalePercent(entity.getSalePercent());
        discountPeriodResponse.setCreatedTime(entity.getCreatedTime());
        discountPeriodResponse.setUpdatedTime(entity.getUpdatedTime());
        discountPeriodResponse.setCreatedBy(entity.getCreatedBy());
        discountPeriodResponse.setUpdatedBy(entity.getUpdatedBy());
        discountPeriodResponse.setStartDate(entity.getStartDate());
        discountPeriodResponse.setEndDate(entity.getEndDate());
        discountPeriodResponse.setStatus(entity.getStatus());
        discountPeriodResponse.setIsDeleted(entity.getIsDeleted());
        discountPeriodResponse.setGiftId(entity.getGiftId());
        discountPeriodResponse.setMinPrice(entity.getMinPrice());
        discountPeriodResponse.setTypePeriod(entity.getTypePeriod());

        return discountPeriodResponse;
    }

    @SneakyThrows
    private void validateSalePercent(DiscountPeriodRequest createPeriodRequestDTO) {
        if (createPeriodRequestDTO.getMinPrice() == null ){
            throw new ValidateException(MessageCode.DiscountPeriod.MINPRICE_NOT_NULL, MessageCode.DiscountPeriod.ENTITY);
        }
        if(createPeriodRequestDTO.getSalePercent() == null) {
            throw new ValidateException(MessageCode.DiscountPeriod.SALE_PERCENT_NOT_NULL, MessageCode.DiscountPeriod.ENTITY);
        }
        if (!DateUtils.isNotNumber(createPeriodRequestDTO.getSalePercent())) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.DiscountPeriod.IS_NUMBER, MessageUtils.getMessage(MessageCode.DiscountPeriod.ENTITY)));
        }
        if (Integer.parseInt(createPeriodRequestDTO.getSalePercent()) <= 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.DiscountPeriod.GREATER_THAN_ZERO, MessageUtils.getMessage(MessageCode.DiscountPeriod.ENTITY)));
        }
        if (Integer.parseInt(createPeriodRequestDTO.getSalePercent()) > 65) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.DiscountPeriod.SMALLER_THAN_SIXTY_FIVE, MessageUtils.getMessage(MessageCode.DiscountPeriod.ENTITY)));
        }
        if(createPeriodRequestDTO.getMinPrice().compareTo(String.valueOf(BigDecimal.valueOf(0))) < 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.DiscountPeriod.GREATER_THAN_ZERO, MessageUtils.getMessage(MessageCode.DiscountPeriod.ENTITY)));
        }
    }
}
