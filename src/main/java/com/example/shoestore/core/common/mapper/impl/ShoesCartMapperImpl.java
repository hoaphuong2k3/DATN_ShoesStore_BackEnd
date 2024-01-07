package com.example.shoestore.core.common.mapper.impl;

import com.example.shoestore.core.common.mapper.ShoesCartMapper;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.cart.model.request.ShoesDetailDTO;
import com.example.shoestore.core.sale.cart.model.response.ShoesCartResponse;
import com.example.shoestore.core.sale.cart.repository.ImageCartRepository;
import com.example.shoestore.entity.*;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.repository.ColorRepository;
import com.example.shoestore.repository.ShoesDetailsRepository;
import com.example.shoestore.repository.ShoesRepository;
import com.example.shoestore.repository.SizeRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoesCartMapperImpl implements ShoesCartMapper {
    @Autowired
    private ShoesDetailsRepository shoesDetailsRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ShoesRepository shoesRepository;

    @Autowired
    private ImageCartRepository imageCartRepository;


    @SneakyThrows
    @Override
    public ShoesCart addCartDTOToEntity(ShoesDetailDTO shoesDetailDTO) {
        if (shoesDetailDTO == null) {
            return null;
        }

        ShoesDetail shoesDetail = shoesDetailsRepository.findById(shoesDetailDTO.getId()).orElseThrow(()
                -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Cart.SHOES_NOTNULL)));
        Size size = sizeRepository.findById(shoesDetail.getSizeId()).orElseThrow(()
                -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Size.ENTITY)));
        Color color = colorRepository.findById(shoesDetail.getColorId()).orElseThrow(()
                -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Color.ENTITY)));
        Shoes shoes = shoesRepository.findById(shoesDetail.getShoesId()).orElseThrow(()
                -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Shoes.ENTITY)));
        List<Image> listImage = imageCartRepository.listImage(shoesDetail.getId());

        BigDecimal quantity = BigDecimal.valueOf(shoesDetailDTO.getQuantity());
        BigDecimal totalPrice = shoesDetail.getDiscountPrice().multiply(quantity);

        ShoesCart shoesCart = new ShoesCart();
        shoesCart.setId(shoesDetailDTO.getId());
        shoesCart.setCode(shoesDetail.getCode());
        shoesCart.setName(shoes.getName());
        shoesCart.setSize(size.getName());
        shoesCart.setColor(color.getName());
        shoesCart.setQuantity(shoesDetailDTO.getQuantity());
        shoesCart.setPrice(shoesDetail.getDiscountPrice());
        shoesCart.setTotalPrice(totalPrice);
        if (listImage.isEmpty()) {
            shoesCart.setImage(null);
        } else {
            shoesCart.setImage(listImage.get(0).getImgURI());
        }

        return shoesCart;
    }


    @Override
    public Page<ShoesCartResponse> pageEntityToDTO(Page<ShoesCart> shoesCartPage) {
        if (shoesCartPage == null) {
            return null;
        }
        Page<ShoesCartResponse> page = shoesCartPage.map(shoesCart -> {
            ShoesCartResponse shoesCartResponse = entityToDTO(shoesCart);
            return shoesCartResponse;
        });
        return page;
    }

    @Override
    public Page<ShoesCart> listToPage(List<ShoesCart> list, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, list.size());

        List<ShoesCart> pageContent = list.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), list.size());

    }

    @Override
    public List<ShoesCartResponse> listEntityToDTO(List<ShoesCart> shoesCartList) {
        List<ShoesCartResponse> shoesCartResponseList = shoesCartList.stream()
                .map(entity -> {
                    ShoesCartResponse response = entityToDTO(entity);
                    return response;
                }).collect(Collectors.toList());
        return shoesCartResponseList;
    }

    @Override
    public ShoesCart DTOToEntity(ShoesCartResponse dto) {
        return null;
    }

    @Override
    public ShoesCartResponse entityToDTO(ShoesCart entity) {
        if (entity == null) {
            return null;
        }
        ShoesCartResponse response = new ShoesCartResponse();
        response.setId(entity.getId());
        response.setCode(entity.getCode());
        response.setName(entity.getName());
        response.setQuantity(entity.getQuantity());
        response.setPrice(entity.getPrice());
        response.setTotalPrice(entity.getTotalPrice());
        response.setColor(entity.getColor());
        response.setSize(entity.getSize());
        response.setImage(entity.getImage());
        return response;
    }
}
