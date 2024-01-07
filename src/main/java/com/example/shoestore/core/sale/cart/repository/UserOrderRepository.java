package com.example.shoestore.core.sale.cart.repository;

import com.example.shoestore.repository.OrdersRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("cartOrderRepository")
public interface UserOrderRepository extends OrdersRepository {

    @Query(nativeQuery = true, value = """
            SELECT COUNT(`discount_periods_id`) FROM `database_final_datn_g33321`.`customer_order`
            WHERE `client_id` = :idClient
            """)
    Integer existsByDiscountPeriods(@Param("idClient") Long idClient);
}
