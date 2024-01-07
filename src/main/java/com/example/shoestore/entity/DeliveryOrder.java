package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder extends AuditEntity {

    @Column(name = "code")
    private String code;
    @Column(name = "ship_date")
    private LocalDateTime shipDate;
    @Column(name = "cancellation_date")
    private LocalDateTime cancelltionDate;
    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @Column(name = "delivery_address")
    private String address;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_phone")
    private String recipientPhone;

    @Column(name = "delivery_cost")
    private BigDecimal deliveryCost;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "order_id")
    private Long idOrder;


}
