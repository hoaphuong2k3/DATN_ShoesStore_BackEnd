package com.example.shoestore.entity;

import com.example.shoestore.core.product.admin.dto.response.ShoesDetailSearchResponse;
import com.example.shoestore.core.product.admin.dto.response.ShoesDetailReportExcelResponse;
import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shoes_detail")
@Builder

@SqlResultSetMapping(
        name = "AdminSearchShoesDetailResultMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ShoesDetailSearchResponse.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "size", type = String.class),
                                @ColumnResult(name = "color", type = String.class),
                                @ColumnResult(name = "quantity", type = Integer.class),
                                @ColumnResult(name = "price", type = BigDecimal.class),
                                @ColumnResult(name = "discount_price", type = BigDecimal.class),
                                @ColumnResult(name = "status", type = Integer.class),
                                @ColumnResult(name = "created_by", type = String.class),
                                @ColumnResult(name = "created_time", type = LocalDateTime.class),
                                @ColumnResult(name = "qr_code_uri", type = String.class),
                        }
                )
        }
)

@SqlResultSetMapping(
        name = "StaffShoesDetailReportMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ShoesDetailReportExcelResponse.class,
                        columns = {
                                @ColumnResult(name = "stt", type = Integer.class),
                                @ColumnResult(name = "shoesCode", type = String.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "size", type = String.class),
                                @ColumnResult(name = "color", type = String.class),
                                @ColumnResult(name = "price", type = BigDecimal.class),
                                @ColumnResult(name = "created_by", type = String.class),
                                @ColumnResult(name = "created_time", type = LocalDateTime.class),
                                @ColumnResult(name = "updated_by", type = String.class),
                                @ColumnResult(name = "updated_time", type = LocalDateTime.class)
                        }
                )
        }
)

public class ShoesDetail extends AuditEntity {

    @Column(name = "shoes_id")
    private Long shoesId;

    @Column(name = "code")
    private String code;

    @Column(name = "size_id")
    private Long sizeId;

    @Column(name = "color_id")
    private Long colorId;

    @Column(name = "promo_id")
    private Long promoId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    @Column(name = "qr_code_uri")
    private String QRCodeURI;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}