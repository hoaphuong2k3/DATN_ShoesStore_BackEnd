package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.repository.DiscountRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoucherRepository extends DiscountRepository {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`discounts`
            SET`quantity` = :quantity
            WHERE `id` = :id
            """)
    void updateQuantityVoucher(@Param("quantity") Integer quantity, @Param("id") Long id);
 @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`discounts`
            SET`status` = :status
            WHERE `id` = :id
            """)
    void updateStatusVoucher(@Param("status") Integer status, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT `discounts`.`quantity`
            FROM `database_final_datn_g33321`.`discounts`
            WHERE  `discounts`.`id` = :id
            """)
    Integer getQuantityVoucher(@Param("id") Long id);
}
