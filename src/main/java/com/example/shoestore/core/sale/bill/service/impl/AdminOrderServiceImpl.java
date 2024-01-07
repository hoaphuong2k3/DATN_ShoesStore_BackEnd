package com.example.shoestore.core.sale.bill.service.impl;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.common.mapper.DeliveryOrderMapper;
import com.example.shoestore.core.common.mapper.OrderMapper;
import com.example.shoestore.core.sale.bill.model.request.CreateShoesInCart;
import com.example.shoestore.core.sale.bill.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.bill.model.request.UpdateShoesInCart;
import com.example.shoestore.core.sale.bill.model.response.CartResponse;
import com.example.shoestore.core.sale.bill.model.response.ExportOrder;
import com.example.shoestore.core.sale.bill.model.response.ExportOrderResponse;
import com.example.shoestore.core.sale.bill.model.response.OrderStatusResponse;
import com.example.shoestore.core.sale.bill.repository.AdminOrderRepository;
import com.example.shoestore.core.sale.bill.repository.UserDeliveryOrderRespository;
import com.example.shoestore.core.sale.bill.repository.UserDiscountPeriodRepository;
import com.example.shoestore.core.sale.bill.repository.UserOrderDetailRepository;
import com.example.shoestore.core.sale.bill.service.AdminOrderService;
import com.example.shoestore.core.sale.cart.repository.ShoesDetailRepository;
import com.example.shoestore.entity.DeliveryOrder;
import com.example.shoestore.entity.OrderDetail;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.infrastructure.constants.HeaderTable;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.constants.OrderStatus;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.ExelExportUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {
    @Autowired
    private AdminOrderRepository adminOrderRepository;
    @Autowired
    private UserOrderDetailRepository orderDetailsRepository;
    @Autowired
    private ShoesDetailRepository shoesDetailsRepository;

    @Autowired
    private UserDeliveryOrderRespository deliveryOrderRespository;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;
    @Autowired
    private UserDiscountPeriodRepository discountPeriodRepository;


    @Override
    public Page<OrderStatusResponse> findAll(Integer status, String code, int page, int size) {
        return adminOrderRepository.findByStatus(status, IsDeleted.UNDELETED.getValue(), code, PageRequest.of(page, size));
    }

    @SneakyThrows
    @Override
    public FileResponseDTO exportExcel(String date, Integer status) {

        LocalDate createDateTime = DateUtils.strToLocalDate(date);

        List<ExportOrder> listOrder = adminOrderRepository.findListExport(status, createDateTime);
        List<ExportOrderResponse> listOrderResponse = new ArrayList<>();
        Integer stt = 1;
        for (ExportOrder export : listOrder) {
            ExportOrderResponse exportOrderResponse = new ExportOrderResponse();
            exportOrderResponse.setStt(stt++);
            exportOrderResponse.setCode(export.getCode());
            exportOrderResponse.setNameAdmin(export.getNameAdmin());
            exportOrderResponse.setNameClient(export.getNameClient());
            exportOrderResponse.setTotalMoney(export.getTotalMoney());
            exportOrderResponse.setTotalPayment(export.getTotalPayment());
            exportOrderResponse.setDatePayment(export.getDatePayment());
            exportOrderResponse.setCreatedTime(export.getCreatedTime());
            exportOrderResponse.setPoints(export.getPoints());
            exportOrderResponse.setPaymentMethod(this.switchPaymentMethod(export.getPaymentMethod()));
            exportOrderResponse.setSaleStatus(export.getSaleStatus() ? "Bán tại quầy" : "Bán trực tuyến");
            exportOrderResponse.setStatus(this.switchStatus(export.getStatus()));
            listOrderResponse.add(exportOrderResponse);
        }

        FileResponseDTO byteArrayResource = ExelExportUtils.exportToExcel(Arrays.asList(HeaderTable.CUSTOMER_ORDER), listOrderResponse, "HoaDon.xlsx", "Hóa đơn");
        return byteArrayResource;

    }

    @SneakyThrows
    @Override
    public ResponseDTO updateStatus(Integer status, Long idStaff, Long idOrder) {
        if (idOrder == null) {
            return null;
        }

        if (status == OrderStatus.CANCELED.getValue()) {
            Long idDiscountPeriod = adminOrderRepository.findById(idOrder).get().getIdDiscountPeriods();
            Long idFreeGift = discountPeriodRepository.getIdFreeGift(idDiscountPeriod);
            Integer getQuantity = discountPeriodRepository.getQuantityFreeGift(idFreeGift);
            if (getQuantity != null) {
                discountPeriodRepository.updateQuantityFreeGift(getQuantity + 1, idFreeGift);
            }
            List<OrderDetail> orderDetail = orderDetailsRepository.findByIdOrderDetail(idOrder);
            for (OrderDetail detail : orderDetail) {
                Long idShoesDetail = detail.getIdShoesDetails();
                ShoesDetail shoesDetailsOptional = shoesDetailsRepository.findAllByStatus(idShoesDetail).orElseThrow(()
                        -> new ResourceNotFoundException(MessageUtils.getMessage(MessageCode.Cart.SHOES_NOTNULL)));
                Integer quantityInStock = shoesDetailsOptional.getQuantity();
                Integer quantity = detail.getQuantity();
                Integer quantityRemain = quantityInStock + quantity;
                shoesDetailsRepository.updateQuantity(quantityRemain, idShoesDetail);
            }
            adminOrderRepository.updateStatus(OrderStatus.CANCELED.getValue(), idStaff, idOrder);
            return ResponseDTO.success();
        }
        adminOrderRepository.updateStatus(status, idStaff, idOrder);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO updateDeliveryOrder(UpdateDeliveryOrderDTO deliveryOrderDTO) {
        if (deliveryOrderDTO == null) {
            return null;
        }
        DeliveryOrder deliveryOrderResponse = deliveryOrderRespository.findById(deliveryOrderDTO.getId()).get();
        DeliveryOrder deliveryOrder = deliveryOrderMapper.updateDtoToEntity(deliveryOrderDTO);
        deliveryOrder.setCode(deliveryOrderResponse.getCode());
        deliveryOrder.setIdOrder(deliveryOrderResponse.getIdOrder());
        deliveryOrder.setShipDate(deliveryOrderResponse.getShipDate());
        deliveryOrder.setCancelltionDate(deliveryOrderResponse.getCancelltionDate());
        deliveryOrder.setReceivedDate(deliveryOrderResponse.getReceivedDate());
        deliveryOrder.setStatus(deliveryOrderResponse.getStatus());
        deliveryOrder.setIsDeleted(deliveryOrderResponse.getIsDeleted());
        deliveryOrderRespository.save(deliveryOrder);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO changeStatusDeliveryOrder(Long id, Integer status) {
        if (id == null) {
            return null;
        }
        deliveryOrderRespository.updateStatus(status, id);
        return ResponseDTO.success();
    }

    @Override
    public List<CartResponse> listCart(Long idOrder) {
        return adminOrderRepository.listCart(idOrder);
    }

    @Override
    public ResponseDTO deliveryDetail(Long idOrder) {
        return ResponseDTO.success(adminOrderRepository.detailDelivery(idOrder, IsDeleted.UNDELETED.getValue()));
    }

    @SneakyThrows
    @Override
    public ResponseDTO updateShoesInCart(UpdateShoesInCart shoesDTO) {
        if (shoesDTO == null || shoesDTO.getId() == null) {
            return null;
        }
        OrderDetail orderDetail = orderDetailsRepository.findById(shoesDTO.getId()).orElse(null);
        ShoesDetail shoesDetail = shoesDetailsRepository.findById(orderDetail.getIdShoesDetails()).orElse(null);
        Integer newQuantity = orderDetail.getQuantity() + shoesDTO.getQuantity();
        if (newQuantity <= 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MIN));
        }
        if (newQuantity > shoesDetail.getQuantity()) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MIN));
        }
        if (newQuantity > 30) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MAX_15));
        }
        BigDecimal newTotalPrice = BigDecimal.valueOf(newQuantity).multiply(shoesDetail.getDiscountPrice());
        adminOrderRepository.updateQuantity(newQuantity, newTotalPrice, shoesDTO.getId());
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO deleteShoesInCart(Long idShoesInCart) {
        if (orderDetailsRepository.existsById(idShoesInCart)) {
            orderDetailsRepository.deleteById(idShoesInCart);
            return ResponseDTO.success();
        }
        return null;
    }

    @SneakyThrows
    @Override
    public ResponseDTO createShoesInCart(CreateShoesInCart shoesDTO) {
        if (shoesDTO == null || shoesDTO.getIdShoesDetail() == null || shoesDTO.getIdOrder() == null) {
            return null;
        }
        ShoesDetail shoesDetail = shoesDetailsRepository.findById(shoesDTO.getIdShoesDetail()).orElse(null);
        if (shoesDTO.getQuantity() <= 0) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MIN));
        }
        if (shoesDTO.getQuantity() > shoesDetail.getQuantity()) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MIN));
        }
        if (shoesDTO.getQuantity() > 30) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Cart.QUANTITY_MAX_15));
        }
        BigDecimal newTotalPrice = BigDecimal.valueOf(shoesDTO.getQuantity()).multiply(shoesDetail.getDiscountPrice());
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setIdOrder(shoesDTO.getIdOrder());
        orderDetail.setIdShoesDetails(shoesDTO.getIdShoesDetail());
        orderDetail.setQuantity(shoesDTO.getQuantity());
        orderDetail.setTotalPrice(newTotalPrice);
        orderDetail.setIsDeleted(IsDeleted.UNDELETED.getValue());
        orderDetailsRepository.save(orderDetail);
        return ResponseDTO.success();
    }

    @Override
    public ResponseDTO countStatus(Integer status) {
        Integer countStatus = adminOrderRepository.countStatus(status);
        return ResponseDTO.success(countStatus);
    }

    @Override
    public ResponseDTO updateTotalmoney(BigDecimal totalMoney, BigDecimal totalPayment, Long idOrder) {
        adminOrderRepository.udpateTotalMoney(totalMoney, totalPayment, idOrder);
        return ResponseDTO.success();
    }

    private String switchPaymentMethod(Integer patmentMethod) {
        switch (patmentMethod) {
            case 1:
                return "Thanh Toán Khi Nhận Hàng";
            case 2:
                return "VNPay";
            case 3:
                return "Chuyển Khoản";
            case 4:
                return "Tiền Mặt";
            default:
                return "Chưa trả tiền";
        }
    }

    private String switchStatus(Integer status) {
        switch (status) {
            case 0:
                return "Chờ xác nhận";
            case 1:
                return "Chờ ship";
            case 2:
                return "Đang ship";
            case 3:
                return "Giao hàng thành công";
            case 4:
                return "Chờ đổi";
            case 5:
                return "Đổi hàng thành công";
            case 6:
                return "Đã nhân";
            case 7:
                return "Hủy";
            default:
                return "Không xác định";
        }
    }
}
