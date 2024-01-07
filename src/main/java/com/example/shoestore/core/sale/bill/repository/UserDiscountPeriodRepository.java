package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.core.sale.bill.model.response.DiscountPeriodsResponse;
import com.example.shoestore.repository.DiscountPeriodRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDiscountPeriodRepository extends DiscountPeriodRepository {

    @Query(nativeQuery = true, value = """
            SELECT `discount_periods`.`id`,
            	`discount_periods`.`sale_percent` AS salePercent,
            	`discount_periods`.`min_price` AS minPrice,
                `discount_periods`.`status`,
                `discount_periods`.`is_deleted` AS isDeleted,
                `discount_periods`.`type_period` AS typePeriod,
                `free_gift`.`name` AS freeGiftName,
                `free_gift`.`image` AS freeGiftImage
            FROM `database_final_datn_g33321`.`discount_periods`
            LEFT JOIN `database_final_datn_g33321`.`free_gift` ON  `discount_periods`.`gift_id` = `free_gift`.`id`
            WHERE `discount_periods`.`is_deleted` = 0 
            AND `discount_periods`.`status` = 0
            """)
    DiscountPeriodsResponse findDiscountPeriod();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`free_gift`
            SET `quantity` = :quantity
            WHERE `id` = :id
            """)
    void updateQuantityFreeGift(@Param("quantity") Integer quantity, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`free_gift`
            SET `status` = :status
            WHERE `id` = :id
            """)
    void updateStatusFreeGift(@Param("status") Integer status, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT `free_gift`.`quantity`
            FROM `database_final_datn_g33321`.`free_gift`
            WHERE `free_gift`.`id` = :id
            """)
    Integer getQuantityFreeGift(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT `discount_periods`.`gift_id`
            FROM `database_final_datn_g33321`.`discount_periods`
            WHERE `discount_periods`.`id` = :idDiscountPeriod
            """)
    Long getIdFreeGift(@Param("idDiscountPeriod") Long idDiscountPeriod);

}
