package com.example.shoestore.entity;

import com.example.shoestore.entity.base.PrimaryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`exchange`")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false))
})
public class Exchange extends PrimaryEntity {
    @Column(name = "refund_amount", precision = 10)
    private BigDecimal refundAmount;



    @Column(name = "exchange_date")
    private LocalDateTime exchangeDate;

    @Size(max = 200)
    @Column(name = "exchange_reason", length = 200)
    private String exchangeReason;

    @Column(name = "exchange_type")
    private Boolean exchangeType;
    @Column(name = "status")
    private Integer status;

    @Column(name = "order_id")
    private Long idOrder;

}