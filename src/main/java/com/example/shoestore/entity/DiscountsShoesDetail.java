package com.example.shoestore.entity;

import com.example.shoestore.entity.base.PrimaryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "discounts_shoes_detail")
public class DiscountsShoesDetail extends PrimaryEntity {
    @Column(name = "promo_id")
    private Long promoId;

    @Column(name = "shoes_detail_id")
    private Long shoesDetailId;

    @Column(name = "discount_price")
    private BigDecimal discountPrice;

}