package com.example.shoestore.core.sale.store.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.OrderMapper;
import com.example.shoestore.core.sale.bill.model.response.DiscountPeriodsResponse;
import com.example.shoestore.core.sale.bill.model.response.OrderResponse;
import com.example.shoestore.core.sale.bill.repository.UserClientRepository;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.cart.model.response.CheckQuantityResponse;
import com.example.shoestore.core.sale.cart.repository.ShoesDetailRepository;
import com.example.shoestore.core.sale.cart.repository.UserOrderRepository;
import com.example.shoestore.core.sale.store.model.request.*;
import com.example.shoestore.core.sale.store.repository.AdminOrderStoreRepository;
import com.example.shoestore.core.sale.store.repository.AdminDeliveryOrderStoreRespository;
import com.example.shoestore.core.sale.store.repository.AdminOrderDetailStoreRepository;
import com.example.shoestore.core.sale.store.repository.UserDiscountPeriodRepository;
import com.example.shoestore.core.sale.store.service.AdminOrderStoreService;
import com.example.shoestore.entity.*;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminOrderStoreServiceImpl implements AdminOrderStoreService {
    @Autowired
    private AdminOrderStoreRepository adminOrderStoreRepository;
    @Autowired
    private AdminOrderDetailStoreRepository orderDetailsRepository;
    @Autowired
    private ShoesDetailRepository shoesDetailsRepository;
    @Autowired
    private AdminDeliveryOrderStoreRespository deliveryOrderRespository;
    @Autowired
    private UserDiscountPeriodRepository discountPeriodRepository;
    @Autowired
    private UserOrderRepository orderRepository;

    @Autowired
    private UserClientRepository clientRepository;


    @Override
    public ResponseDTO createOrderStore(CreateOrderStoreDTO orderStoreDTO) {
        if (orderStoreDTO == null) {
            return null;
        }
        Integer points = this.genaratePoints(orderStoreDTO.getTotalMoney());
        Order order = new Order();
        order.setCode(GenerateCode.generateCodeInvoice());
        order.setTotalMoney(orderStoreDTO.getTotalMoney());
        order.setTotalPayment(orderStoreDTO.getTotalPayment());
        order.setPaymentMethod(orderStoreDTO.getPaymentMethod());
        order.setSaleStatus(SaleStoreStatus.SALE_STORE.getValue());
        if (orderStoreDTO.getIdClient() != null) {
            order.setPoints(points);
        }
        if (orderStoreDTO.getDeliveryOrderDTO() == null) {
            order.setStatus(OrderStatus.RECEIVED.getValue());
        } else {
            order.setStatus(OrderStatus.AWAITING_SHIPPING.getValue());
        }
        order.setIsDeleted(IsDeleted.UNDELETED.getValue());
        order.setIdClient(orderStoreDTO.getIdClient());
        order.setIdAccount(orderStoreDTO.getIdStaff());
        order.setIdDiscountPeriods(orderStoreDTO.getIdDiscountPeriods());
        Order saveOrder = adminOrderStoreRepository.save(order);
        this.createCart(orderStoreDTO.getShoesInCart(), saveOrder.getId());
        this.createDelivery(orderStoreDTO.getDeliveryOrderDTO(), saveOrder.getId());
        this.changeQuantity(orderStoreDTO.getShoesInCart());
        this.usingPoints(orderStoreDTO.getIdClient(), orderStoreDTO.getUsingPoints(), points, orderStoreDTO.getTotalPoints());
        this.updateQuantityFreeGift();
        return ResponseDTO.success(saveOrder);

    }

    @Override
    public ResponseDTO findDiscountPeriod() {
        return ResponseDTO.success(discountPeriodRepository.findDiscountPeriod());
    }

    @SneakyThrows
    private void createCart(List<CreateShoesInCart> createCart, Long idOrder) {
        if (idOrder == null || createCart.isEmpty()) {
            throw new ValidateException(MessageCode.Cart.SHOES_NOTNULL);
        }

        List<OrderDetail> listCart = new ArrayList<>();
        List<Long> listIdShoes = createCart.stream().map(CreateShoesInCart::getIdShoesDetail).collect(Collectors.toList());
        List<ShoesDetail> listShoes = shoesDetailsRepository.findAllByListId(listIdShoes);
        if (listShoes.size() != listIdShoes.size()) {
            throw new ValidateException(MessageCode.Cart.SHOES_NOTNULL);
        }

//        List<CheckQuantityResponse> listQuantity = shoesDetailsRepository.checkQuantity(listIdShoes);
//        Map<Long, Integer> mapQuantity = new HashMap<>();
//        for (CheckQuantityResponse response : listQuantity) {
//            if(!mapQuantity.containsKey(response.getId())){
//               List<CheckQuantityResponse> listlist = new ArrayList<>();
//               listlist.add(response);
//               mapQuantity.put(response.getId(),);
//            }
//        }
        for (CreateShoesInCart shoesInCart : createCart) {
            ShoesDetail shoesDetail = shoesDetailsRepository.findById(shoesInCart.getIdShoesDetail()).orElse(null);

            if (shoesInCart.getQuantity() > shoesDetail.getQuantity()) {
                throw new ValidateException(MessageCode.Cart.QUANTITY_MAX);
            }

            BigDecimal totalPrice = BigDecimal.valueOf(shoesInCart.getQuantity()).multiply(shoesDetail.getDiscountPrice());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(shoesInCart.getQuantity());
            orderDetail.setTotalPrice(totalPrice);
            orderDetail.setPrice(shoesDetail.getDiscountPrice());
            orderDetail.setIdOrder(idOrder);
            orderDetail.setIdShoesDetails(shoesInCart.getIdShoesDetail());
            orderDetail.setIsDeleted(IsDeleted.UNDELETED.getValue());

            listCart.add(orderDetail);
        }
        orderDetailsRepository.saveAll(listCart);
    }

    private ResponseDTO createDelivery(CreateDeliveryDTO createDeliveryDTO, Long idOrder) {
        if (createDeliveryDTO == null || idOrder == null) {
            return null;
        }
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setCode(GenerateCode.code("PGH"));
        deliveryOrder.setAddress(createDeliveryDTO.getAddress());
        deliveryOrder.setRecipientName(createDeliveryDTO.getRecipientName());
        deliveryOrder.setRecipientPhone(createDeliveryDTO.getRecipientPhone());
        deliveryOrder.setDeliveryCost(createDeliveryDTO.getDeliveryCost());
        deliveryOrder.setStatus(DeliveryOrderStatus.WAITING_SHIP.getValue());
        deliveryOrder.setIsDeleted(IsDeleted.UNDELETED.getValue());
        deliveryOrder.setIdOrder(idOrder);
        deliveryOrderRespository.save(deliveryOrder);
        return ResponseDTO.success();
    }

    @SneakyThrows
    private void changeQuantity(List<CreateShoesInCart> createShoesInCarts) {
        if (createShoesInCarts.isEmpty()) {
            throw new ValidateException(MessageCode.Cart.SHOES_NOTNULL);
        }
        for (CreateShoesInCart shoes : createShoesInCarts) {
            ShoesDetail shoesDetailsOptional = shoesDetailsRepository.findAllByStatus(shoes.getIdShoesDetail()).orElseThrow(()
                    -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Cart.SHOES_NOTNULL)));
            Integer quantityInStock = shoesDetailsOptional.getQuantity();
            Integer quantity = shoes.getQuantity();
            Integer quantityRemain = quantityInStock - quantity;
            shoesDetailsRepository.updateQuantity(quantityRemain, shoes.getIdShoesDetail());
            if (quantityRemain == 0) {
                shoesDetailsRepository.updateStatus(ShoesStatus.OUT_OF_STOCK.getValue(), shoes.getIdShoesDetail());
            }
        }

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

    private void usingPoints(Long idClient, Boolean usingPoints, Integer points, Integer totalPoints) {
        if (idClient != null && usingPoints) {
            clientRepository.updatePointClient(totalPoints, idClient);
        }
        if (idClient != null) {
            Integer getPoints = clientRepository.getTotalPoints(idClient);
            if (getPoints != null && points != null) {
                clientRepository.updatePointClient(getPoints + points, idClient);
            } else {
                clientRepository.updatePointClient(points, idClient);

            }
        }
    }

    private void updateQuantityFreeGift() {
        Long idDiscountPeriod = discountPeriodRepository.getIdDiscountPeriod();

        if(DataUtils.isNotNull(idDiscountPeriod)){
            Optional<DiscountPeriod> discountPeriod = discountPeriodRepository.findById(idDiscountPeriod);
            if (discountPeriod.isPresent()) {
                Integer quantityFreeGift = discountPeriodRepository.getQuantityFreeGift(discountPeriod.get().getGiftId());
                if (quantityFreeGift != null && quantityFreeGift > 0) {
                    discountPeriodRepository.updateQuantityFreeGift(quantityFreeGift - 1, discountPeriod.get().getGiftId());
                }

                Integer newQuantityFG = discountPeriodRepository.getQuantityFreeGift(discountPeriod.get().getGiftId());
                if (newQuantityFG != null && newQuantityFG == 0) {
                    discountPeriodRepository.updateStatusFreeGift(ShoesStatus.OUT_OF_STOCK.getValue(), discountPeriod.get().getGiftId());
                }
            }
        }
    }

}
