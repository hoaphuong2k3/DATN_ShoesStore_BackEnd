package com.example.shoestore.core.sale.delivery.service.impl;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.core.sale.delivery.model.request.CreateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.request.UpdateDeliveryOrderDTO;
import com.example.shoestore.core.sale.delivery.model.response.DeliveryOrderResponse;
import com.example.shoestore.core.sale.delivery.repository.AdminDeliveryOrderRespository;
import com.example.shoestore.core.sale.delivery.service.DeliveryService;
import com.example.shoestore.entity.DeliveryOrder;
import com.example.shoestore.infrastructure.constants.DeliveryOrderStatus;
import com.example.shoestore.infrastructure.constants.IsDeleted;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.constants.RegexPattern;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.DataFormatUtils;
import com.example.shoestore.infrastructure.utils.DateUtils;
import com.example.shoestore.infrastructure.utils.GenerateCode;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private AdminDeliveryOrderRespository deliveryOrderRespository;


    @SneakyThrows
    @Override
    public ResponseDTO createDelivery(CreateDeliveryOrderDTO deliveryOrderDTO) {
        if (deliveryOrderDTO == null) {
            throw new ValidateException(MessageCode.DeliveryOrder.DELIVERY_ORDER_NULL);
        }

        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setCode(GenerateCode.code("PGH"));
        deliveryOrder.setAddress(DataFormatUtils.formatString(deliveryOrderDTO.getDeliveryAddress()));
        deliveryOrder.setRecipientName(DataFormatUtils.formatString(deliveryOrderDTO.getRecipientName()));
        if (!deliveryOrderDTO.getRecipientPhone().matches(RegexPattern.REGEX_PHONENUMBER)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PHONENUMBER_INVALID));
        } else {
            deliveryOrder.setRecipientPhone(DataFormatUtils.formatString(deliveryOrderDTO.getRecipientPhone()));
        }
        deliveryOrder.setDeliveryCost(deliveryOrderDTO.getDeliveryCost());
        deliveryOrder.setStatus(DeliveryOrderStatus.WAITING_SHIP.getValue());
        deliveryOrder.setIsDeleted(IsDeleted.UNDELETED.getValue());
        deliveryOrder.setIdOrder(deliveryOrderDTO.getIdOrder());
        deliveryOrderRespository.save(deliveryOrder);
        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO updateDelivery(UpdateDeliveryOrderDTO deliveryOrderDTO) {
        if (deliveryOrderDTO == null) {
            throw new ValidateException(MessageCode.DeliveryOrder.DELIVERY_ORDER_NULL);
        }
        String code = deliveryOrderRespository.findById(deliveryOrderDTO.getId()).get().getCode();
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(deliveryOrderDTO.getId());
        deliveryOrder.setCode(code);
        deliveryOrder.setShipDate(DateUtils.strToLocalDateTime(deliveryOrderDTO.getShipDate()));
        deliveryOrder.setCancelltionDate(DateUtils.strToLocalDateTime(deliveryOrderDTO.getCancelltionDate()));
        deliveryOrder.setReceivedDate(DateUtils.strToLocalDateTime(deliveryOrderDTO.getReceivedDate()));
        deliveryOrder.setAddress(DataFormatUtils.formatString(deliveryOrderDTO.getDeliveryAddress()));
        deliveryOrder.setRecipientName(DataFormatUtils.formatString(deliveryOrderDTO.getRecipientName()));
        if (!deliveryOrderDTO.getRecipientPhone().matches(RegexPattern.REGEX_PHONENUMBER)) {
            throw new ValidateException(MessageUtils.getMessage(MessageCode.Accounts.PHONENUMBER_INVALID));
        } else {
            deliveryOrder.setRecipientPhone(deliveryOrderDTO.getRecipientPhone());
        }
        deliveryOrder.setDeliveryCost(deliveryOrderDTO.getDeliveryCost());
        deliveryOrder.setStatus(deliveryOrderDTO.getStatus());
        deliveryOrder.setIsDeleted(IsDeleted.UNDELETED.getValue());
        deliveryOrder.setIdOrder(deliveryOrderDTO.getIdOrder());
        deliveryOrderRespository.save(deliveryOrder);
        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO deleteDelivery(Long id) {
        if (id == null) {
            throw new ValidateException(MessageCode.DeliveryOrder.DELIVERY_ORDER_NULL);
        }
        DeliveryOrder deliveryOrder = deliveryOrderRespository.findById(id).orElse(null);
        deliveryOrder.setId(deliveryOrder.getId());
        deliveryOrder.setIsDeleted(IsDeleted.DELETED.getValue());
        deliveryOrderRespository.save(deliveryOrder);
        return ResponseDTO.success();
    }

    @SneakyThrows
    @Override
    public ResponseDTO detailDelivery(Long id) {
        if (id == null) {
            throw new ValidateException(MessageCode.DeliveryOrder.DELIVERY_ORDER_NULL);
        }
        return ResponseDTO.success(deliveryOrderRespository.findByIdDelivery(id));
    }

    @Override
    public Page<DeliveryOrderResponse> pageDelivery(int page, int size, String code, String date, Integer status) {

        return deliveryOrderRespository.pageDelivery(IsDeleted.UNDELETED.getValue(), status, code, DateUtils.strToLocalDate(date), PageRequest.of(page, size));
    }

    @Override
    public ResponseDTO updateStatus(Integer status, Long id) {
        if (status == DeliveryOrderStatus.SHIPPING.getValue()) {
            deliveryOrderRespository.updateStatus(status, id);
            deliveryOrderRespository.updateShipDate(LocalDateTime.now(), id);
            return ResponseDTO.success();

        }
        if (status == DeliveryOrderStatus.CANCEL.getValue()) {
            deliveryOrderRespository.updateStatus(status, id);
            deliveryOrderRespository.updateCancellationDate(LocalDateTime.now(), id);
            return ResponseDTO.success();

        }

        deliveryOrderRespository.updateStatus(status, id);
        deliveryOrderRespository.updateReceivedDate(LocalDateTime.now(), id);
        return ResponseDTO.success();

    }
}
