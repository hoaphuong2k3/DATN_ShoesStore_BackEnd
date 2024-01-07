package com.example.shoestore.core.sale.cart.repository;

import com.example.shoestore.core.sale.cart.model.response.CheckQuantityResponse;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.repository.ShoesDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public interface ShoesDetailRepository extends ShoesDetailsRepository {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`shoes_detail`
            SET `quantity` = :quantity
            WHERE `id` = :id
            """)
    void updateQuantity(@Param("quantity") Integer quantity, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`shoes_detail`
            SET `status` =:status
            WHERE `id` = :id
            """)
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT `shoes_detail`.`id`,
                `shoes_detail`.`shoes_id`,
                `shoes_detail`.`promo_id`,
                `shoes_detail`.`size_id`,
                `shoes_detail`.`color_id`,
                `shoes_detail`.`code`,
                `shoes_detail`.`price`,
                `shoes_detail`.`quantity`,
                `shoes_detail`.`qr_code_uri`,
                `shoes_detail`.`status`,
                `shoes_detail`.`created_by`,
                `shoes_detail`.`updated_by`,
                `shoes_detail`.`created_time`,
                `shoes_detail`.`updated_time`,
                `shoes_detail`.`is_deleted`,
                `shoes_detail`.`discount_price`
            FROM `database_final_datn_g33321`.`shoes_detail`
            WHERE  `shoes_detail`.`status` = 1  
            AND  `shoes_detail`.`is_deleted` = false 
            AND `shoes_detail`.`id` = :id
            """)
    Optional<ShoesDetail> findAllByStatus(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT `shoes_detail`.`id`,
                `shoes_detail`.`shoes_id`,
                `shoes_detail`.`promo_id`,
                `shoes_detail`.`size_id`,
                `shoes_detail`.`color_id`,
                `shoes_detail`.`code`,
                `shoes_detail`.`price`,
                `shoes_detail`.`quantity`,
                `shoes_detail`.`status`,
                `shoes_detail`.`qr_code_uri`,
                `shoes_detail`.`created_by`,
                `shoes_detail`.`updated_by`,
                `shoes_detail`.`created_time`,
                `shoes_detail`.`updated_time`,
                `shoes_detail`.`is_deleted`,
                `shoes_detail`.`discount_price`
            FROM `database_final_datn_g33321`.`shoes_detail`
            WHERE  `shoes_detail`.`status` = 1  
            AND  `shoes_detail`.`is_deleted` = false 
            AND `shoes_detail`.`id` IN :ids
            """)
    List<ShoesDetail> findAllByListId(@Param("ids") List<Long> ids);

    @Query(nativeQuery = true, value = """
            SELECT `shoes_detail`.`id`,
                `shoes_detail`.`quantity`
            FROM `database_final_datn_g33321`.`shoes_detail`
            WHERE  `shoes_detail`.`status` = 1  
            AND  `shoes_detail`.`is_deleted` = false 
            AND `shoes_detail`.`id` IN :ids
            """)
    List<CheckQuantityResponse> checkQuantity(@Param("ids") List<Long> ids);

}
