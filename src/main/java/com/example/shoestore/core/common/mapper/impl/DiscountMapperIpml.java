package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.DiscountMapper;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.DiscountRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.PromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.request.UpdatePromoRequest;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountResponse;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiscountMapperIpml implements DiscountMapper {

    @Override
    public Discount createDTOToEntity(DiscountRequest createDiscountDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(createDiscountDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(createDiscountDTO.getEndDate(), formatter);
        if(createDiscountDTO == null){
            return null;
        }

        Discount discount = new Discount();
        discount.setCode(GenerateCode.code("DCT"));
        discount.setName(createDiscountDTO.getName());
        if(createDiscountDTO.getSale() == true){
            discount.setSalePercent(Integer.valueOf(createDiscountDTO.getSalePercent()));
            discount.setSalePrice(null);
        } else {
            discount.setSalePrice(BigDecimal.valueOf(Long.parseLong(createDiscountDTO.getSalePrice())));
            discount.setSalePercent(null);
        }
        discount.setQuantity(createDiscountDTO.getQuantity());
        discount.setDiscountType(DiscountType.VOUCHERS.getValue());
        discount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        discount.setStatus(DiscountStatus.WAITING.getValue());
        discount.setCreatedTime(LocalDateTime.now());
        discount.setCreatedBy(createDiscountDTO.getCreatedBy());
        discount.setUpdatedBy(createDiscountDTO.getUpdatedBy());
        discount.setMinPrice(BigDecimal.valueOf(Long.parseLong(createDiscountDTO.getMinPrice())));
        discount.setDescription(createDiscountDTO.getDescription());
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);
        discount.setAvailable(AvailableStatus.YES.getValue());

        return discount;
    }

    @Override
    public Discount updateDTOToEntity(DiscountRequest updateDiscountsDTO) {
        if(updateDiscountsDTO == null){
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(updateDiscountsDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(updateDiscountsDTO.getEndDate(), formatter);

        Discount discount = new Discount();
        discount.setId(updateDiscountsDTO.getId());
        discount.setCode(updateDiscountsDTO.getCode());
        discount.setName(updateDiscountsDTO.getName());
        if(updateDiscountsDTO.getSale() == true){
            discount.setSalePercent(Integer.valueOf(updateDiscountsDTO.getSalePercent()));
            discount.setSalePrice(null);
        } else {
            discount.setSalePrice(BigDecimal.valueOf(Long.parseLong(updateDiscountsDTO.getSalePrice())));
            discount.setSalePercent(null);
        }
        discount.setStatus(updateDiscountsDTO.getStatus());
        discount.setIsDeleted(updateDiscountsDTO.getIsDeleted());
        discount.setQuantity(updateDiscountsDTO.getQuantity());
        discount.setUpdatedTime(LocalDateTime.now());
        discount.setCreatedBy(updateDiscountsDTO.getCreatedBy());
        discount.setUpdatedBy(updateDiscountsDTO.getUpdatedBy());
        discount.setMinPrice(BigDecimal.valueOf(Long.parseLong(updateDiscountsDTO.getMinPrice())));
        discount.setDescription(updateDiscountsDTO.getDescription());
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);
        discount.setDiscountType(DiscountType.PROMOS.getValue());
        discount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        discount.setStatus(updateDiscountsDTO.getStatus());
        if(updateDiscountsDTO.getQuantity() > 1){
            discount.setAvailable(AvailableStatus.YES.getValue());
        } else {
            discount.setAvailable(AvailableStatus.NO.getValue());
        }

        return discount;
    }

    @Override
    public Discount createPromoDTOToEntity(PromoRequest promoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(promoDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(promoDTO.getEndDate(), formatter);
        if(promoDTO == null){
            return null;
        }

        Discount discount = new Discount();
        discount.setCode(GenerateCode.code("PRM"));
        discount.setName(promoDTO.getName());
        if(promoDTO.getSale() == true){
            discount.setSalePercent(Integer.valueOf(promoDTO.getSalePercent()));
            discount.setSalePrice(null);
        } else {
            discount.setSalePrice(BigDecimal.valueOf(Long.parseLong(promoDTO.getSalePrice())));
            discount.setSalePercent(null);
        }
        discount.setQuantity(promoDTO.getQuantity());
        discount.setCreatedTime(LocalDateTime.now());
        discount.setCreatedBy(promoDTO.getCreatedBy());
        discount.setUpdatedBy(promoDTO.getUpdatedBy());
        discount.setMinPrice(BigDecimal.valueOf(Long.parseLong(promoDTO.getMinPrice())));
        discount.setDescription(promoDTO.getDescription());
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);
        discount.setDiscountType(DiscountType.PROMOS.getValue());
        discount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        discount.setStatus(DiscountStatus.WAITING.getValue());

        return discount;
    }

    @Override
    public Discount updatePromoDTOToEntity(PromoRequest promoDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        LocalDateTime startDate = LocalDateTime.parse(promoDTO.getStartDate(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(promoDTO.getEndDate(), formatter);
        if(promoDTO == null){
            return null;
        }

        Discount discount = new Discount();
        discount.setId(promoDTO.getId());
        discount.setCode(promoDTO.getCode());
        discount.setName(promoDTO.getName());
        if(promoDTO.getSale() == true){
            discount.setSalePercent(Integer.valueOf(promoDTO.getSalePercent()));
            discount.setSalePrice(null);
        } else {
            discount.setSalePrice(BigDecimal.valueOf(Long.parseLong(promoDTO.getSalePrice())));
            discount.setSalePercent(null);
        }
        discount.setQuantity(promoDTO.getQuantity());
        discount.setCreatedTime(promoDTO.getCreatedTime());
        discount.setUpdatedTime(LocalDateTime.now());
        discount.setCreatedBy(promoDTO.getCreatedBy());
        discount.setUpdatedBy(promoDTO.getUpdatedBy());
        discount.setMinPrice(BigDecimal.valueOf(Long.parseLong(promoDTO.getMinPrice())));
        discount.setDescription(promoDTO.getDescription());
        discount.setStartDate(startDate);
        discount.setEndDate(endDate);
        discount.setIsDeleted(IsDeleted.UNDELETED.getValue());
        discount.setDiscountType(DiscountType.PROMOS.getValue());
        discount.setStatus(promoDTO.getStatus());

        return discount;
    }

    @Override
    public List<DiscountResponse> entityListToDTOList(List<Discount> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }

        List<DiscountResponse> dtoList = new ArrayList<>();

        entityList.forEach(entity->{
            DiscountResponse dto = entityToDTO(entity);
            if (dto != null) {
                dtoList.add(dto);
            }
        });

        return dtoList;
    }

    @Override
    public Page<DiscountResponse> pageEntityTODTO(Page<Discount> pageDiscount) {
        if (pageDiscount == null) {
            return null;
        }
        Page<DiscountResponse> discountResponsePage = pageDiscount.map(discount -> {
            DiscountResponse discountResponse = entityToDTO(discount);
            return discountResponse;
        });
        return discountResponsePage;
    }

    @Override
    public Discount DTOToEntity(DiscountResponse dto) {

        return null;
    }

    @Override
    public DiscountResponse entityToDTO(Discount entity) {
        if (entity == null) {
            return null;
        }

        DiscountResponse discountResponse = new DiscountResponse();
        discountResponse.setId(entity.getId());
        discountResponse.setCode(entity.getCode());
        discountResponse.setName(entity.getName());
        discountResponse.setQuantity(entity.getQuantity());
        discountResponse.setSalePrice(entity.getSalePrice());
        discountResponse.setSalePercent(entity.getSalePercent());
        discountResponse.setCreatedTime(entity.getCreatedTime());
        discountResponse.setUpdatedTime(entity.getUpdatedTime());
        discountResponse.setCreatedBy(entity.getCreatedBy());
        discountResponse.setUpdatedBy(entity.getUpdatedBy());
        discountResponse.setMinPrice(entity.getMinPrice());
        discountResponse.setDescription(entity.getDescription());
        discountResponse.setStartDate(entity.getStartDate());
        discountResponse.setEndDate(entity.getEndDate());
        discountResponse.setStatus(entity.getStatus());
        discountResponse.setIsDeleted(entity.getIsDeleted());
        discountResponse.setDiscountType(entity.getDiscountType());
        discountResponse.setAvailable(entity.getAvailable());

        return discountResponse;
    }
}
