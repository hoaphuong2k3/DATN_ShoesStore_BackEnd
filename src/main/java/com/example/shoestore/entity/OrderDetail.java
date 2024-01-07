package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import com.example.shoestore.entity.base.PrimaryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
@Builder
public class OrderDetail extends PrimaryEntity {

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "order_id")
    private Long idOrder;

    @Column(name = "shoes_details_id")
    private Long idShoesDetails;

}
