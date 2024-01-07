package com.example.shoestore.core.product.user.repository;

import com.example.shoestore.core.product.user.dto.response.UserShoesDetailResponse;
import com.example.shoestore.core.product.user.dto.response.UserShoesDetailResponseById;
import com.example.shoestore.core.sale.cart.repository.ShoesDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserShoesDetailRepository extends ShoesDetailRepository {

    @Query(nativeQuery = true, value = """
            SELECT shdt.id,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle,
            skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion,
            cl.name as color, sz.name as size,
            shdt.price,shdt.discount_price as discountPrice,shdt.quantity, sh.description,dc.sale_percent as salePercent 
            FROM shoes_detail shdt
            LEFT JOIN shoes sh ON sh.id = shdt.shoes_id
            LEFT JOIN brand br ON br.id = sh.brand_id
            LEFT JOIN origin o ON o.id = sh.origin_id
            LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
            LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
            LEFT JOIN sole sl ON sl.id = sh.sole_id
            LEFT JOIN lining ln ON ln.id = sh.lining_id
            LEFT JOIN toe t ON t.id = sh.toe_id
            LEFT JOIN cushion cs ON cs.id = sh.cushion_id
            LEFT JOIN color cl on cl.id = shdt.color_id
            LEFT JOIN size sz on sz.id = shdt.size_id
            LEFT JOIN discounts dc ON shdt.promo_id = dc.id
            WHERE sh.id = :shoesId AND cl.id = :colorId AND sz.id = :sizeId 
            """)
    UserShoesDetailResponse getOne(@Param("shoesId") Long shoesId, @Param("colorId") Long colorId, @Param("sizeId") Long sizeId);

    @Query(nativeQuery = true, value = """
            SELECT shdt.id,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle,
            skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion,
            cl.name as color, sz.name as size,
            shdt.price,shdt.discount_price as discountPrice,shdt.quantity, sh.description
            FROM shoes_detail shdt
            LEFT JOIN shoes sh ON sh.id = shdt.shoes_id
            LEFT JOIN brand br ON br.id = sh.brand_id
            LEFT JOIN origin o ON o.id = sh.origin_id
            LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
            LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
            LEFT JOIN sole sl ON sl.id = sh.sole_id
            LEFT JOIN lining ln ON ln.id = sh.lining_id
            LEFT JOIN toe t ON t.id = sh.toe_id
            LEFT JOIN cushion cs ON cs.id = sh.cushion_id
            LEFT JOIN color cl on cl.id = shdt.color_id
            LEFT JOIN size sz on sz.id = shdt.size_id
            WHERE shdt.qr_code_uri = :qrCode
            """)
    UserShoesDetailResponse getOneByQRCode(@Param("qrCode") String qrCode);

    @Query(nativeQuery = true, value = """
            SELECT shdt.id,cl.id as colorId, sz.id as sizeId, sh.id as shoesId
            FROM shoes_detail shdt
            LEFT JOIN shoes sh ON sh.id = shdt.shoes_id
            LEFT JOIN color cl on cl.id = shdt.color_id
            LEFT JOIN size sz on sz.id = shdt.size_id
            WHERE shdt.id = :id
            """)
    UserShoesDetailResponseById getOneById(@Param("id") Long id);

}
