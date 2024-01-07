package com.example.shoestore.core.sale.cart.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.ShoesCartMapper;
import com.example.shoestore.core.sale.bill.model.response.DiscountPeriodsResponse;
import com.example.shoestore.core.sale.bill.repository.UserDiscountPeriodRepository;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.cart.model.request.CheckoutDTO;
import com.example.shoestore.core.sale.cart.model.request.DeleteShoesCartDTO;
import com.example.shoestore.core.sale.cart.model.request.ShoesDetailDTO;
import com.example.shoestore.core.sale.cart.model.response.CheckoutResponse;
import com.example.shoestore.core.sale.cart.model.response.ShoesCartResponse;
import com.example.shoestore.core.sale.cart.repository.ShoesCartRepository;
import com.example.shoestore.core.sale.cart.repository.ShoesDetailRepository;
import com.example.shoestore.core.sale.cart.repository.UserClientRepository;
import com.example.shoestore.core.sale.cart.repository.UserOrderRepository;
import com.example.shoestore.core.sale.cart.service.ShoesCartService;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.infrastructure.constants.DiscountPeriodType;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.repository.DiscountRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ShoesCartServiceImpl implements ShoesCartService {
    @Autowired
    private ShoesCartRepository shoesCartRepository;

    @Autowired
    private ShoesDetailRepository shoesDetailsRepository;

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private UserDiscountPeriodRepository discountPeriodRepository;
    @Autowired
    private UserOrderRepository orderRepository;
    @Autowired
    private UserClientRepository clientRepository;


    @Autowired
    private ShoesCartMapper mapper;

    private Map<Long, CheckoutResponse> mapCheckout = new HashMap<>();
    private static ShoesCart shoesCart = new ShoesCart();


    @SneakyThrows
    @Override
    public ResponseDTO addToCart(ShoesDetailDTO shoesDetailDTO) {
        if (shoesCartRepository.findAll(shoesDetailDTO.getKey()).size() > 15) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.CART_FUll));
        }
        if (checkQuantity(shoesDetailDTO)) {
            shoesCart = mapper.addCartDTOToEntity(shoesDetailDTO);
            Integer newQuantity = changeQuantity(shoesDetailDTO);
            BigDecimal newPrice = changePrice(shoesDetailDTO);

            if (shoesCartRepository.existsById(shoesDetailDTO.getKey(), shoesDetailDTO.getId())) {
                shoesCartRepository.updateQuantity(shoesDetailDTO.getKey(), shoesDetailDTO.getId(), newQuantity, newPrice);
            } else {
                shoesCartRepository.save(shoesDetailDTO.getKey(), shoesCart);
            }
        }
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO removeValueInRedis(Long key, Long id) {
        shoesCartRepository.deleteItemsInValue(key, id);
        return ResponseDTO.success();
    }

    @Override
    public Page<ShoesCartResponse> findShoesInCart(int page, int size, Long key) {
        List<ShoesCart> shoesCartList = shoesCartRepository.findAll(key);
        Page<ShoesCart> shoesCartPage = mapper.listToPage(shoesCartList, page, size);

        return mapper.pageEntityToDTO(shoesCartPage);

    }


    @Override
    public ResponseDTO updateQuantity(ShoesDetailDTO shoesDetailDTO) {
        if (checkQuantity(shoesDetailDTO)) {
            ShoesDetail shoesDetail = getShoesDetail(shoesDetailDTO);
            BigDecimal newPrice = shoesDetail.getDiscountPrice().multiply(BigDecimal.valueOf(shoesDetailDTO.getQuantity()));
            shoesCartRepository.updateQuantity(shoesDetailDTO.getKey(), shoesDetailDTO.getId(), shoesDetailDTO.getQuantity(), newPrice);
            return ResponseDTO.success(shoesCartRepository.findById(shoesDetailDTO.getKey(), shoesDetailDTO.getId()));
        }
        return null;
    }

    @Override
    public ResponseDTO checkout(CheckoutDTO checkoutDTO) {
        DiscountPeriodsResponse discountPeriods = discountPeriodRepository.findDiscountPeriod();
        List<ShoesCart> shoesCartList = shoesCartRepository.findByList(checkoutDTO.getKey(), checkoutDTO.getListShoesCart());
        List<ShoesCartResponse> shoesCartResponseList = mapper.listEntityToDTO(shoesCartList);

        Integer totalPoints = clientRepository.getTotalPoints(checkoutDTO.getKey());
        BigDecimal totalMoney = this.totalMoney(shoesCartList);
        BigDecimal totalPayment = this.totalPayment(totalMoney, discountPeriods, checkoutDTO.getKey());
        Integer points = this.genaratePoints(totalMoney);


        CheckoutResponse response = new CheckoutResponse();
        response.setShoesCart(shoesCartResponseList);
        response.setTotalMoney(totalMoney);
        response.setTotalPayment(totalPayment);
        response.setPoints(points);
        response.setTotalPoints(totalPoints);
        if (discountPeriods != null && totalMoney != totalPayment) {
            Long idFreeGift = discountPeriodRepository.getIdFreeGift(discountPeriods.getId());
            Integer quantityFreeGift = discountPeriodRepository.getQuantityFreeGift(idFreeGift);
            response.setIdDiscountPeriod(discountPeriods.getId());
            response.setPeriodType(discountPeriods.getTypePeriod());
            if (DataUtils.isNotNull(quantityFreeGift)) {
                if (quantityFreeGift > 0) {
                    response.setFreeGiftName(discountPeriods.getFreeGiftName());
                    response.setFreeGiftImage(discountPeriods.getFreeGiftImage());
                }
            }
        }
        mapCheckout.put(checkoutDTO.getKey(), response);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO findCheckout(Long key) {
        CheckoutResponse checkoutResponse = mapCheckout.get(key);
        return ResponseDTO.success(checkoutResponse);
    }

    @Override
    public ResponseDTO buyNow(ShoesDetailDTO shoesDetailDTO) {
        this.addToCart(shoesDetailDTO);

        ShoesCart shoesCart = shoesCartRepository.findById(shoesDetailDTO.getKey(), shoesDetailDTO.getId());
        List<ShoesCart> shoesCartList = new ArrayList<>();
        shoesCartList.add(shoesCart);

        DiscountPeriodsResponse discountPeriods = discountPeriodRepository.findDiscountPeriod();
        BigDecimal totalMoney = this.totalMoney(shoesCartList);
        BigDecimal totalPayment = this.totalPayment(totalMoney, discountPeriods, shoesDetailDTO.getKey());
        Integer points = this.genaratePoints(totalMoney);

        Integer totalPoints = clientRepository.getTotalPoints(shoesDetailDTO.getKey());

        Long idDiscountPeriods = null;

        if (DataUtils.isNotNull(discountPeriods)) {
            idDiscountPeriods = discountPeriods.getId();
        }

        Long idFreeGift = discountPeriodRepository.getIdFreeGift(idDiscountPeriods);
        Integer quantityFreeGift = discountPeriodRepository.getQuantityFreeGift(idFreeGift);
        ShoesCartResponse shoesCartResponse = mapper.entityToDTO(shoesCart);

        List<ShoesCartResponse> listShoesCart = new ArrayList<>();
        listShoesCart.add(shoesCartResponse);

        CheckoutResponse response = new CheckoutResponse();
        response.setShoesCart(listShoesCart);
        response.setTotalMoney(totalMoney);
        response.setTotalPayment(totalPayment);
        response.setPoints(points);
        response.setTotalPoints(totalPoints);
        if (discountPeriods != null && totalMoney != totalPayment) {
            response.setIdDiscountPeriod(discountPeriods.getId());
            response.setPeriodType(discountPeriods.getTypePeriod());
            if (quantityFreeGift > 0) {
                response.setFreeGiftName(discountPeriods.getFreeGiftName());
                response.setFreeGiftImage(discountPeriods.getFreeGiftImage());
            }
        }
        mapCheckout.put(shoesDetailDTO.getKey(), response);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO deleteShoesInCheckout(Long key, Long id) {
        List<ShoesCartResponse> listShoesCart = mapCheckout.get(key).getShoesCart();
        for (int i = 0; i < listShoesCart.size(); i++) {
            if (listShoesCart.get(i).getId().equals(id)) {
                listShoesCart.remove(i);
            }
        }
        BigDecimal priceShoesCart = shoesCartRepository.findById(key, id).getTotalPrice();
        BigDecimal totalMoney = mapCheckout.get(key).getTotalMoney().subtract(priceShoesCart);

        CheckoutResponse response = new CheckoutResponse();
        response.setShoesCart(listShoesCart);
        response.setTotalMoney(totalMoney);
        mapCheckout.put(id, response);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO deleteListShoesInCart(Long key, DeleteShoesCartDTO deleteShoesCartDTO) {
        shoesCartRepository.deleteListItems(key, deleteShoesCartDTO.getId());
        return ResponseDTO.success();
    }

    private BigDecimal changePrice(ShoesDetailDTO shoesDetailDTO) {
        ShoesDetail shoesDetail = getShoesDetail(shoesDetailDTO);

        if (shoesCartRepository.existsById(shoesDetailDTO.getKey(), shoesDetailDTO.getId())) {
            BigDecimal quantity = BigDecimal.valueOf(changeQuantity(shoesDetailDTO));
            return shoesDetail.getDiscountPrice().multiply(quantity);
        }
        return shoesDetail.getDiscountPrice().multiply(BigDecimal.valueOf(shoesDetailDTO.getQuantity()));
    }

    private Integer changeQuantity(ShoesDetailDTO shoesDetailDTO) {
        List<ShoesCart> shoesCartList = shoesCartRepository.findAll(shoesDetailDTO.getKey());
        Optional<ShoesCart> shoesCartOptional = shoesCartList.stream().
                filter(item -> item.getId().equals(shoesDetailDTO.getId())).findFirst();
        if (shoesCartOptional.isEmpty()) {
            return null;
        }
        return shoesCartOptional.get().getQuantity() + shoesDetailDTO.getQuantity();
    }

    @SneakyThrows
    private ShoesDetail getShoesDetail(ShoesDetailDTO shoesDetailDTO) {
        if (shoesDetailsRepository.existsById(shoesDetailDTO.getId())) {
            return shoesDetailsRepository.findAllByStatus(shoesDetailDTO.getId()).orElseThrow(()
                    -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Cart.SHOES_NOTNULL)));
        }
        throw new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Cart.SHOES_NOTNULL));
    }

    @SneakyThrows
    private boolean checkQuantity(ShoesDetailDTO shoesDetailDTO) {
        ShoesDetail shoesDetail = getShoesDetail(shoesDetailDTO);
        Integer changeQuantity = changeQuantity(shoesDetailDTO);

        if (shoesCartRepository.existsById(shoesDetailDTO.getKey(), shoesDetailDTO.getId())) {
            if (changeQuantity <= 0) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MIN));
            } else if (changeQuantity != null && changeQuantity > shoesDetail.getQuantity()) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MAX));
            } else if (changeQuantity != null && changeQuantity > 15) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MAX_15));
            }

        } else {
            if (shoesDetailDTO.getQuantity() <= 0) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MIN));
            } else if (shoesDetailDTO.getQuantity() > shoesDetail.getQuantity()) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MAX));
            } else if (changeQuantity != null && changeQuantity > 15) {
                throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MAX_15));
            }

        }
        return true;
    }

    private BigDecimal totalMoney(List<ShoesCart> list) {
        List<BigDecimal> listMoney = list.stream().map(ShoesCart::getTotalPrice).collect(Collectors.toList());
        BigDecimal totalMoney = listMoney.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalMoney;
    }

    private BigDecimal totalPayment(BigDecimal totalMoney, DiscountPeriodsResponse discountPeriods, Long idClient) {
        Integer existsByDiscountPeriod = orderRepository.existsByDiscountPeriods(idClient);
        if (existsByDiscountPeriod == 0) {
            if (discountPeriods != null) {
                if (discountPeriods.getTypePeriod() == DiscountPeriodType.ORDER.getValue()) {
                    if (totalMoney.compareTo(discountPeriods.getMinPrice()) >= 0) {
                        BigDecimal percent = BigDecimal.valueOf(discountPeriods.getSalePercent());
                        BigDecimal salePercent = percent.divide(BigDecimal.valueOf(100));
                        BigDecimal totalMoneyAfter = totalMoney.multiply(salePercent);

                        return totalMoney.subtract(totalMoneyAfter);
                    }
                }
            }
        }
        return totalMoney;
    }

    private Integer genaratePoints(BigDecimal totalMoney) {
        if (totalMoney == null) {
            return null;
        }
        BigDecimal number = BigDecimal.valueOf(10000000);
        BigDecimal minPercent = BigDecimal.valueOf(0.01); // 1%
        BigDecimal maxPercent = BigDecimal.valueOf(0.02); // 2%
        if (totalMoney.compareTo(number) > 0) {
            return totalMoney.multiply(maxPercent).intValue();
        }
        return totalMoney.multiply(minPercent).intValue();
    }


}