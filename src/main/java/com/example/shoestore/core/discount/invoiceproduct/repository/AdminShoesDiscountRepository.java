package com.example.shoestore.core.discount.invoiceproduct.repository;

import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.ShoesDetailInfo;
import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.ShoesPromoProjection;
import com.example.shoestore.entity.DiscountsShoesDetail;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.repository.ShoesDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AdminShoesDiscountRepository extends ShoesDetailsRepository {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail SET promo_id = :promoId WHERE id = :id """)
    void saveShoe(@Param("id") Long shoeId, @Param("promoId") Long promoId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail SET discount_price = price, promo_id = NULL WHERE id = :id """)
    void stopShoe(@Param("id") Long idShoe);

    @Query(nativeQuery = true, value = """
            SELECT COUNT(*) FROM shoes_detail shdt WHERE shdt.is_deleted = 0 AND shdt.id IN (:shoesIds) """)
    long countExistPromoByShoesIds(@Param("shoesIds") List<Long> shoesIds);

    @Query(nativeQuery = true, value = """
            SELECT shdt.price FROM shoes_detail shdt WHERE shdt.is_deleted = 0 AND shdt.id IN (:shoesIds) """)
    BigDecimal getPrice(@Param("shoesIds") Long shoesIds);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail set discount_price = :price WHERE id = :shoesId """)
    void addDiscountPrice(@Param("price") BigDecimal discountPrice,@Param("shoesId") Long shoesIds);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail set discount_price = price WHERE promo_id = :shoesId """)
    void updateDiscountPrice(@Param("shoesId") Long shoesIds);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail set promo_id = null WHERE promo_id = :shoesId """)
    void updatePromoId(@Param("shoesId") Long shoesIds);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail set promo_id = :promoId WHERE id = :shoesId """)
    void updatePromoIdByShoeId(@Param("promoId") Long promoId, @Param("shoesId") Long shoesIds);

    @Query(nativeQuery = true, value = """
           SELECT dsd.shoes_detail_id as idShoeDetail, sh.id,sh.code,sh.name,br.name as brand,o.name as origin,dsst.name as designStyle,
                                                                                    skt.name as skinType,sl.name as sole,ln.name as lining,t.name as toe,cs.name as cushion,
                                                                                    sh.img_name,sh.img_uri,s.name as size,c.name as color,dsd.discount_price as discountPrice,
                                                                                    shdt.price as originPrice,sh.created_by,sh.created_time FROM shoes sh
                                                                                    LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id
                                                                                    LEFT JOIN brand br ON br.id = sh.brand_id
                                                                                    LEFT JOIN origin o ON o.id = sh.origin_id
                                                                                    LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
                                                                                    LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
                                                                                    LEFT JOIN sole sl ON sl.id = sh.sole_id
                                                                                    LEFT JOIN lining ln ON ln.id = sh.lining_id
                                                                                    LEFT JOIN toe t ON t.id = sh.toe_id
                                                                                    LEFT JOIN cushion cs ON cs.id = sh.cushion_id
                                                                                    LEFT JOIN size s ON s.id = shdt.size_id
                                                                                    LEFT JOIN color c ON c.id = shdt.color_id
                                                                                    LEFT JOIN discounts_shoes_detail dsd ON dsd.shoes_detail_id = shdt.id
                                                                                    WHERE sh.is_deleted = 0 and dsd.promo_id = :id 
                                                                                    HAVING 1 = 1
            """)
    List<ShoesDetailInfo> getOneCustom(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            select * from shoes_detail where is_deleted = 0 and id IN (:shoeDetailId) """)
    List<ShoesDetail> findPrice(@Param("shoeDetailId") List<Long> shoeDetailId);

    @Query(nativeQuery = true, value = """
            select shdt.promo_id as promoId,shdt.code as shoeDetailCode,s.code as shoeCode from shoes_detail shdt
            left join shoes s on shdt.shoes_id = s.id
            where shdt.is_deleted = 0 and shdt.id = :shoeDetailId """)
    ShoesPromoProjection findPriceIsUsing(@Param("shoeDetailId") Long shoeDetailId);
}
