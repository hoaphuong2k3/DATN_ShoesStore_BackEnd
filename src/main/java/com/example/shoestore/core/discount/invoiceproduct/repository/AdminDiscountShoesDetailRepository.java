package com.example.shoestore.core.discount.invoiceproduct.repository;

import com.example.shoestore.entity.DiscountsShoesDetail;
import com.example.shoestore.repository.DiscountsShoesDetailRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AdminDiscountShoesDetailRepository extends DiscountsShoesDetailRepository {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE discounts_shoes_detail set discount_price = :price WHERE id = :shoesId """)
    void updateDiscountShoesPrice(@Param("price") BigDecimal discountPrice, @Param("shoesId") Long shoesIds);

    @Transactional
    @Query(nativeQuery = true, value = """
            select * from discounts_shoes_detail where promo_id = :promoId 
            """)
    List<DiscountsShoesDetail> getByPromoId(@Param("promoId") Long promoId);

    @Transactional
    @Query(nativeQuery = true, value = """
            select shoes_detail_id from discounts_shoes_detail where promo_id = :promoId 
            """)
    List<Long> getByShoesDetailId(@Param("promoId") Long promoId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            INSERT INTO discounts_shoes_detail VALUES (:promo_id,:shoes_detail_id,:discount_price)""")
    void addDiscountShoesPrice(@Param("promo_id") Long promoId,@Param("promo_id") Long shoesIds,@Param("discount_price") BigDecimal discountPrice);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            delete from discounts_shoes_detail where promo_id = :promoId """)
    void deleteDiscountShoesPrice(@Param("promoId") Long promoId);

    @Query(nativeQuery = true, value = """
            select price from discounts_shoes_detail where promo_id = :promoId"" """)
    List<BigDecimal> findPrice(@Param("promoId") List<Long> promoId);
}
