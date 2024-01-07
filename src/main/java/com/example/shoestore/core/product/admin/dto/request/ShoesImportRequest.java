package com.example.shoestore.core.product.admin.dto.request;

import com.example.shoestore.infrastructure.utils.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShoesImportRequest {

    @ExcelColumn(value = 0,dataType = Integer.class,applyMinMaxCheck = false,header = "STT")
    private Integer stt;

    @ExcelColumn(value = 1,dataType = String.class,maxLength = 50,header = "Tên giày")
    private String name;

    @ExcelColumn(value = 2,dataType = Long.class,applyMinMaxCheck = false,header = "Phong cách thiết kế")
    private Long designStyleId;

    @ExcelColumn(value = 3, dataType = Long.class,applyMinMaxCheck = false,header = "Thương hiệu")
    private Long brandId;

    @ExcelColumn(value = 4,dataType = Long.class,applyMinMaxCheck = false,header = "Lót giày")
    private Long liningId;

    @ExcelColumn(value = 5,dataType = Long.class,applyMinMaxCheck = false,header = "Đế giày")
    private Long soleId;

    @ExcelColumn(value = 6,dataType = Long.class,applyMinMaxCheck = false,header = "Loại da")
    private Long skinTypeId;

    @ExcelColumn(value = 7,dataType = Long.class,applyMinMaxCheck = false,header = "Xuất xứ")
    private Long originId;

    @ExcelColumn(value = 8,dataType = Long.class,applyMinMaxCheck = false,header = "Đệm giày")
    private Long cushionId;

    @ExcelColumn(value = 9,dataType = Long.class,applyMinMaxCheck = false,header = "Mũi giày")
    private Long toeId;

    @ExcelColumn(value = 10,dataType = String.class,applyMinMaxCheck = false,header = "URI Image")
    private String imgURI;

    @ExcelColumn(value = 11,dataType = String.class,applyMinMaxCheck = false,header = "Tên Ảnh")
    private String imgName;

    @ExcelColumn(value = 12,dataType = String.class,applyMinMaxCheck = false,header = "Mô tả")
    private String description;
}
