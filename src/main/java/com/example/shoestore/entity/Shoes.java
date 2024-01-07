package com.example.shoestore.entity;

import com.example.shoestore.core.product.admin.dto.response.ShoesSearchResponse;
import com.example.shoestore.core.product.user.dto.response.UserShoesSearchResponse;
import com.example.shoestore.entity.base.AuditEntity;
import com.example.shoestore.infrastructure.utils.ExcelColumn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "shoes")

@SqlResultSetMapping(
        name = "AdminSearchShoesResultMapping",
        classes = {
                @ConstructorResult(
                        targetClass = ShoesSearchResponse.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "brand", type = String.class),
                                @ColumnResult(name = "origin", type = String.class),
                                @ColumnResult(name = "designStyle", type = String.class),
                                @ColumnResult(name = "skinType", type = String.class),
                                @ColumnResult(name = "sole", type = String.class),
                                @ColumnResult(name = "lining", type = String.class),
                                @ColumnResult(name = "toe", type = String.class),
                                @ColumnResult(name = "cushion", type = String.class),
                                @ColumnResult(name = "img_name", type = String.class),
                                @ColumnResult(name = "img_uri", type = String.class),
                                @ColumnResult(name = "priceMax", type = BigDecimal.class),
                                @ColumnResult(name = "priceMin", type = BigDecimal.class),
                                @ColumnResult(name = "discountPriceMax", type = BigDecimal.class),
                                @ColumnResult(name = "discountPriceMin", type = BigDecimal.class),
                                @ColumnResult(name = "totalQuantity", type = Long.class),
                                @ColumnResult(name = "totalRecord", type = Long.class),
                                @ColumnResult(name = "created_by", type = String.class),
                                @ColumnResult(name = "created_time", type = LocalDateTime.class)
                        }
                )
        }
)

@SqlResultSetMapping(
        name = "UserSearchShoesResultMapping",
        classes = {
                @ConstructorResult(
                        targetClass = UserShoesSearchResponse.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "brand", type = String.class),
                                @ColumnResult(name = "origin", type = String.class),
                                @ColumnResult(name = "designStyle", type = String.class),
                                @ColumnResult(name = "skinType", type = String.class),
                                @ColumnResult(name = "sole", type = String.class),
                                @ColumnResult(name = "lining", type = String.class),
                                @ColumnResult(name = "toe", type = String.class),
                                @ColumnResult(name = "cushion", type = String.class),
                                @ColumnResult(name = "img_name", type = String.class),
                                @ColumnResult(name = "img_uri", type = String.class),
                                @ColumnResult(name = "priceMax", type = BigDecimal.class),
                                @ColumnResult(name = "priceMin", type = BigDecimal.class),
                                @ColumnResult(name = "discountPriceMax", type = BigDecimal.class),
                                @ColumnResult(name = "discountPriceMin", type = BigDecimal.class),
                        }
                )
        }
)

public class Shoes extends AuditEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "design_style_id")
    private Long designStyleId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "lining_id")
    private Long liningId;

    @Column(name = "sole_id")
    private Long soleId;

    @Column(name = "skin_type_id")
    private Long skinTypeId;

    @Column(name = "origin_id")
    private Long originId;

    @Column(name = "cushion_id")
    private Long cushionId;

    @Column(name = "toe_id")
    private Long toeId;

    @Column(name = "img_uri")
    private String imgURI;

    @Column(name = "img_name")
    private String imgName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}