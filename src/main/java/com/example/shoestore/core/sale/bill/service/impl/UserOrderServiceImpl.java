package com.example.shoestore.core.sale.bill.service.impl;


import com.example.shoestore.core.common.BaseService;
import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.OrderMapper;
import com.example.shoestore.core.sale.bill.model.request.BillPrintRequestDTO;
import com.example.shoestore.core.sale.bill.model.request.CreateDeliveryOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.CreateOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateStatusDTO;
import com.example.shoestore.core.sale.bill.model.response.*;
import com.example.shoestore.core.sale.bill.repository.*;
import com.example.shoestore.core.sale.bill.service.UserOrderService;
import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import com.example.shoestore.core.sale.cart.repository.ShoesCartRepository;
import com.example.shoestore.core.sale.cart.repository.ShoesDetailRepository;
import com.example.shoestore.entity.*;
import com.example.shoestore.infrastructure.constants.*;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.*;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    private UserOrderRepository orderRepository;

    @Autowired
    private ShoesCartRepository shoesCartRepository;

    @Autowired
    private UserOrderDetailRepository orderDetailRepository;

    @Autowired
    private ShoesDetailRepository shoesDetailsRepository;

    @Autowired
    private UserDeliveryOrderRespository deliveryOrderRespository;
    @Autowired
    private UserDiscountPeriodRepository discountPeriodRepository;

    @Autowired
    private UserVoucherRepository voucherRepository;
    @Autowired
    private UserClientRepository clientRepository;


    @Autowired
    private OrderMapper mapper;

    private static Order order = new Order();

    @SneakyThrows
    @Override
    public ResponseDTO createBill(CreateOrderDTO orderDTO) {
        List<StatusShoesDetail> getStatus = orderRepository.getStatus(orderDTO.getIdShoesDetail());
        for (StatusShoesDetail x : getStatus) {
            if (x.getStatus().equals(ShoesStatus.ON_BUSINESS.getValue())) {
                order = mapper.createDTOToEntity(orderDTO);
                order.setCode(GenerateCode.generateCodeInvoice());
                order.setIdAccount(orderDTO.getIdStaff());
                order.setIdClient(orderDTO.getIdClient());
                order.setIdDiscountPeriods(orderDTO.getIdDisCountPeriod());
                order.setIdDiscount(orderDTO.getIdVoucher());
                order.setPaymentMethod(orderDTO.getPaymentMethod());
                order.setTotalMoney(orderDTO.getTotalMoney());
                order.setTotalPayment(orderDTO.getTotalPayment());
                order.setPoints(orderDTO.getPoints());
                order.setStatus(OrderStatus.WAITING_CONFIRM.getValue());
                order.setIsDeleted(IsDeleted.UNDELETED.getValue());
                Order saveOrder = orderRepository.save(order);
                if (orderDTO.getUsingPoints()) {
                    this.usingPoints(orderDTO.getIdClient(), orderDTO.getTotalPoints());
                }
                this.createDeliveryOrders(orderDTO.getDeliveryOrderDTO(), saveOrder.getId());
                this.addCartInDB(saveOrder.getId(), orderDTO.getIdClient(), orderDTO.getIdShoesDetail());
                this.changeQuantityInStock(orderDTO.getIdClient(), orderDTO.getIdShoesDetail());
                this.deleteByKey(orderDTO.getIdClient(), orderDTO.getIdShoesDetail());
                this.updateQuantityFreeGift(orderDTO.getIdDisCountPeriod());
                this.updateQuantityVoucher(orderDTO.getIdVoucher());
                OrderResponse orderResponse = mapper.entityToDTO(saveOrder);
                return ResponseDTO.success(orderResponse);
            } else {
                throw new ValidateException(MessageCode.ShoesDetail.SHOES_STATUS_OUT_OF_STOCK);
            }
        }
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO updateBill(UpdateStatusDTO statusDTO) {
        if (statusDTO.getStatus() == OrderStatus.CANCELED.getValue()) {
            orderRepository.updateStatus(OrderStatus.CANCELED.getValue(), statusDTO.getIdOrder());
            return ResponseDTO.success();
        }
        Integer points = orderRepository.getPointsOrder(statusDTO.getIdOrder());
        Integer totalPoints = clientRepository.getTotalPoints(statusDTO.getIdClient());
        if (totalPoints == null) {
            clientRepository.updatePointClient(points, statusDTO.getIdClient());
        }
        clientRepository.updatePointClient(totalPoints + points, statusDTO.getIdClient());

        orderRepository.updateStatus(OrderStatus.RECEIVED.getValue(), statusDTO.getIdOrder());
        return ResponseDTO.success();
    }

    @Override
    public Page<UserOrderDetailResponse> findOrder(Long idClient, Integer status, int page, int size) {
        List<UserOrderDetailResponse> listOrderDetail = this.listOrderDetail(idClient, status);
        return this.listToPage(listOrderDetail, PageRequest.of(page, size));
    }

    @Override
    public ResponseDTO detailOrder(Long idOrder) {
        if (idOrder == null) {
            return null;
        }
        Optional<Order> orderOptional = orderRepository.findById(idOrder);
        if (orderOptional.isPresent()) {
            DeliveryResponse delivery = orderDetailRepository.findDelivery(orderOptional.get().getId());
            List<CartResponse> listCart = orderDetailRepository.listCart(orderOptional.get().getId());
            UserDetailBillResponse detailBillResponse = new UserDetailBillResponse();
            detailBillResponse.setId(orderOptional.get().getId());
            detailBillResponse.setCode(orderOptional.get().getCode());
            detailBillResponse.setTotalMoney(orderOptional.get().getTotalMoney());
            detailBillResponse.setTotalPayment(orderOptional.get().getTotalPayment());
            detailBillResponse.setCreateTime(orderOptional.get().getCreatedTime());
            detailBillResponse.setStatus(orderOptional.get().getStatus());
            if (delivery != null) {
                detailBillResponse.setShipPrice(delivery.getDeliveryCost());
                detailBillResponse.setCancellationDate(delivery.getCancellationDate());
                detailBillResponse.setShipDate(delivery.getShipDate());
                detailBillResponse.setReceivedDate(delivery.getReceivedDate());
                detailBillResponse.setNameDelivery(delivery.getRecipientName());
                detailBillResponse.setAddressDelivery(delivery.getDeliveryAddress());
                detailBillResponse.setPhonenumberDelivery(delivery.getRecipientPhone());
            }
            if (!listCart.isEmpty()) {
                detailBillResponse.setListCart(listCart);
            }
            return ResponseDTO.success(detailBillResponse);
        }
        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public FileResponseDTO printBill(Long idOrder, BillPrintRequestDTO requestDTO) {
        FileResponseDTO fileResponseDTO = new FileResponseDTO();


        Optional<Order> orderOptional = orderRepository.findById(idOrder);
        List<CartResponse> listCart = orderRepository.listCart(orderOptional.get().getId());
        DeliveryResponse deliveryResponse = orderRepository.detailDelivery(orderOptional.get().getId());
        BillPrintResponse billPrintResponse = new BillPrintResponse();
        if (orderOptional.isPresent()) {
            billPrintResponse.setCode(orderOptional.get().getCode());
            billPrintResponse.setTotalMoney(orderOptional.get().getTotalMoney());
            billPrintResponse.setTotalPayment(orderOptional.get().getTotalPayment());
            if(DataUtils.isNotNull(deliveryResponse)){
                billPrintResponse.setPriceShip(DataUtils.isNull(deliveryResponse.getDeliveryCost()) ? null : deliveryResponse.getDeliveryCost());
            }
            billPrintResponse.setPriceDiscountPeriod(requestDTO.getPriceDiscountPeriod());
            billPrintResponse.setAddressDelivery(requestDTO.getAddressDelivery());
            billPrintResponse.setListCart(listCart);
            Integer paymentMethodByBill = orderOptional.get().getPaymentMethod();
            if (paymentMethodByBill.equals(PaymentMethod.BANK_TRANSFER.getValue())) {
                billPrintResponse.setPaymentMethod(PaymentMethod.getDescription(PaymentMethod.BANK_TRANSFER.getValue()));
            } else if (paymentMethodByBill.equals(PaymentMethod.CASH_PAYMENT.getValue())) {
                billPrintResponse.setPaymentMethod(PaymentMethod.getDescription(PaymentMethod.CASH_PAYMENT.getValue()));
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream inputStream = getClass().getResourceAsStream("/templates/pdf/PrintBill.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("logoPath", getClass().getResourceAsStream("/templates/image/logo.png"));
            parameters.put("code", billPrintResponse.getCode());
            parameters.put("totalMoney", billPrintResponse.getTotalMoney());
            parameters.put("addressDelivery", billPrintResponse.getAddressDelivery());
            parameters.put("timeNow", new Timestamp(System.currentTimeMillis()));
            parameters.put("accountName", BaseService.getUserLogin().getFullname());
            parameters.put("nameClient", requestDTO.getNameClient());
            parameters.put("phoneClient", requestDTO.getPhoneClient());
            parameters.put("totalPaymentIntoWord", NumToViet.num2String(billPrintResponse.getTotalPayment().longValue()));
            parameters.put("accumulatedCoin", requestDTO.getAccumulatedCoins());
            parameters.put("coinRefund", requestDTO.getCoinRefund());
            parameters.put("totalPayment", billPrintResponse.getTotalPayment());
            parameters.put("priceDiscountPeriod", billPrintResponse.getPriceDiscountPeriod());
            parameters.put("dataTable", new JRBeanCollectionDataSource(billPrintResponse.getListCart()));

            fileResponseDTO.setFileName("PrintBill.pdf");
            fileResponseDTO.setByteArrayResource(JasperUtils.exportJasperReportToPdf(jasperReport, parameters, bos));
        }
        return fileResponseDTO;
    }

    private List<UserOrderDetailResponse> listOrderDetail(Long idClient, Integer status) {
        List<UserOrderDetailResponse> listOrderDetail = new ArrayList<>();
        List<OrderStatusResponse> listOrder = orderDetailRepository.findByClient(status, idClient);

        for (OrderStatusResponse bill : listOrder) {
            DeliveryResponse deliveryOrder = orderDetailRepository.findDelivery(bill.getId());
            List<CartResponse> listCart = orderDetailRepository.listCart(bill.getId());
            UserOrderDetailResponse orderDetailResponse = new UserOrderDetailResponse();
            orderDetailResponse.setId(bill.getId());
            orderDetailResponse.setCode(bill.getCode());
            orderDetailResponse.setTotalPayment(bill.getTotalPayment());
            orderDetailResponse.setCreateTime(bill.getCreatedTime());
            if (deliveryOrder != null) {
                orderDetailResponse.setCancellationDate(deliveryOrder.getCancellationDate());
                orderDetailResponse.setReceivedDate(deliveryOrder.getReceivedDate());
                orderDetailResponse.setShipDate(deliveryOrder.getShipDate());
            }
            orderDetailResponse.setStatus(bill.getStatus());
            orderDetailResponse.setListCart(listCart);
            listOrderDetail.add(orderDetailResponse);
        }

        return listOrderDetail;
    }

    private void addCartInDB(Long idOrder, Long idClient, List<Long> idShoes) {
        List<ShoesDetail> shoesDetail = shoesDetailsRepository.findAllByListId(idShoes);
        List<ShoesCart> shoesCartList = shoesCartRepository.findByList(idClient, idShoes);
        List<OrderDetail> orderDetailList = shoesCartList.stream()
                .map(redis -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setIdShoesDetails(redis.getId());
                    orderDetail.setQuantity(redis.getQuantity());
                    orderDetail.setTotalPrice(redis.getTotalPrice());
                    orderDetail.setIdOrder(idOrder);
                    for (ShoesDetail detail : shoesDetail) {
                        orderDetail.setPrice(detail.getDiscountPrice());
                    }
                    orderDetail.setIsDeleted(IsDeleted.UNDELETED.getValue());
                    return orderDetail;
                }).collect(Collectors.toList());
        orderDetailRepository.saveAll(orderDetailList);

    }

    @SneakyThrows
    private void changeQuantityInStock(Long idClient, List<Long> idShoes) {
        List<ShoesCart> shoesCartList = shoesCartRepository.findByList(idClient, idShoes);
        for (ShoesCart shoesCart : shoesCartList) {
            ShoesDetail shoesDetailsOptional = shoesDetailsRepository.findAllByStatus(shoesCart.getId()).orElseThrow(()
                    -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Cart.SHOES_NOTNULL)));
            Integer quantityInStock = shoesDetailsOptional.getQuantity();
            Integer quantity = shoesCart.getQuantity();
            Integer quantityRemain = quantityInStock - quantity;
            shoesDetailsRepository.updateQuantity(quantityRemain, shoesCart.getId());
            if (quantityRemain == 0) {
                shoesDetailsRepository.updateStatus(ShoesStatus.OUT_OF_STOCK.getValue(), shoesCart.getId());
            }
        }
    }

    private void deleteByKey(Long idAccount, List<Long> idShoes) {
        shoesCartRepository.deleteListItems(idAccount, idShoes);
    }

    @SneakyThrows
    private void createDeliveryOrders(CreateDeliveryOrderDTO deliveryOrderDTO, Long idOrder) {
        if (deliveryOrderDTO == null) {
            throw new ValidateException(MessageCode.DeliveryOrder.DELIVERY_ORDER_NULL);
        }

        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setCode(GenerateCode.code("PGH"));
        deliveryOrder.setAddress(deliveryOrderDTO.getAddress());
        deliveryOrder.setRecipientName(deliveryOrderDTO.getRecipientName());
        deliveryOrder.setRecipientPhone(deliveryOrderDTO.getRecipientPhone());
        deliveryOrder.setDeliveryCost(deliveryOrderDTO.getDeliveryCost());
        deliveryOrder.setStatus(DeliveryOrderStatus.WAITING_SHIP.getValue());
        deliveryOrder.setIsDeleted(IsDeleted.UNDELETED.getValue());
        deliveryOrder.setIdOrder(idOrder);
        deliveryOrderRespository.save(deliveryOrder);
    }

    private Page<UserOrderDetailResponse> listToPage(List<UserOrderDetailResponse> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    private void updateQuantityFreeGift(Long idDiscountPeriod) {
        if (idDiscountPeriod != null) {
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

    private void updateQuantityVoucher(Long idVoucher) {
        if (idVoucher != null) {
            Integer quantityVoucher = voucherRepository.getQuantityVoucher(idVoucher);
            if (quantityVoucher != null && quantityVoucher > 0) {
                voucherRepository.updateQuantityVoucher(quantityVoucher - 1, idVoucher);
            }
            Integer newQuantityVoucher = voucherRepository.getQuantityVoucher(idVoucher);
            if (newQuantityVoucher != null && newQuantityVoucher == 0) {
                voucherRepository.updateStatusVoucher(DiscountStatus.STOP.getValue(), idVoucher);
            }
        }
    }

    private void usingPoints(Long idClient, Integer totalPoints) {
        if (idClient != null && totalPoints != null) {
            clientRepository.updatePointClient(totalPoints, idClient);
        }
    }

}


