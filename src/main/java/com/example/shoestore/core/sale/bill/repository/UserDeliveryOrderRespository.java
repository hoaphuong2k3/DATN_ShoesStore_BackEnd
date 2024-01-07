package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.repository.DeliveryOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeliveryOrderRespository extends DeliveryOrderRepository {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`delivery_orders`
            SET `status` = :status
            WHERE `id` = :id
            """)
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);
}
