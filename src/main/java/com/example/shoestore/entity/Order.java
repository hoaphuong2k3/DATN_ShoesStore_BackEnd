package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_order")
@Builder
public class Order extends AuditEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "total_payment")
    private BigDecimal totalPayment;

    @Column(name = "date_payment")
    private LocalDateTime datePayment;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "sale_status")
    private Boolean saleStatus;

    @Column(name = "status")
    private Integer status;
    @Column(name = "points")
    private Integer points;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "member_account_id")
    private Long idAccount;

    @Column(name = "client_id")
    private Long idClient;

    @Column(name = "discount_id")
    private Long idDiscount;
    @Column(name = "discount_periods_id")
    private Long idDiscountPeriods;


}
