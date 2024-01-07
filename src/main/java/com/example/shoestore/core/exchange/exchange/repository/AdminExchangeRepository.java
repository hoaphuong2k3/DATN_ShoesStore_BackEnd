package com.example.shoestore.core.exchange.exchange.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminExchangeRepository extends ExchangeShoesRepository {


//    @Query(nativeQuery = true, value = """
//        SELECT `exchange`.`id`,
//            `exchange`.`refund_amount`,
//            `exchange`.`quantity`,
//            `exchange`.`exchange_date`,
//            `exchange`.`exchange_reason`,
//            `exchange`.`exchange_type`,
//            `exchange`.`status`,
//            `exchange`.`shoes_detail_id`,
//            `exchange`.`order_id`
//        FROM `database_final_datn_g33321`.`exchange`
//        WHERE exchange_type = :type AND status = :status
//""")
//    Exchange

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`exchange`
            SET`status` = :status
            WHERE `id` = :id;
            """)
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);
}
