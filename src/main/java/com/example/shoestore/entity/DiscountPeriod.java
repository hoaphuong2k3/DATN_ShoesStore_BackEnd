package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "discount_periods")
public class DiscountPeriod extends AuditEntity {

    @Column(name = "sale_percent")
    private Integer salePercent;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "gift_id")
    private Long giftId;

    @Column(name = "type_period")
    private Integer typePeriod;

}