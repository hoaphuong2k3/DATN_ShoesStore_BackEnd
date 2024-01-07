package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "discounts")
@Builder
public class Discount extends AuditEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "sale_percent")
    private Integer salePercent;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "min_price")
    private BigDecimal minPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "discount_type")
    private Integer discountType;

    @Column(name = "available")
    private Integer available;


}