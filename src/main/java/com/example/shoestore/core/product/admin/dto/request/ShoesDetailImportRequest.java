package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.utils.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShoesDetailImportRequest {

    @ExcelColumn(value = 0,dataType = Integer.class,applyMinMaxCheck = false,header = "STT")
    private Integer stt;

    @ExcelColumn(value = 1, dataType = Long.class,applyMinMaxCheck = false,header = "Sản phẩm")
    private Long shoesId;

    @ExcelColumn(value = 2, dataType = Long.class,applyMinMaxCheck = false,header = "Size")
    private Long sizeId;

    @ExcelColumn(value = 3, dataType = Long.class,applyMinMaxCheck = false,header = "Màu")
    private Long colorId;

    // min 5.000.000, max 100.000.000
    @ExcelColumn(value = 4, dataType = BigDecimal.class,min = 1000000,max = 15000000,header = "Giá")
    private BigDecimal price;

    @ExcelColumn(value = 5, dataType = Integer.class,min = 1,nullable = false,header = "Số lượng")
    private Integer quantity;

    @ExcelColumn(value = 6, dataType = Integer.class,max = 1,header = "Trạng thái")
    private Integer status;

}
