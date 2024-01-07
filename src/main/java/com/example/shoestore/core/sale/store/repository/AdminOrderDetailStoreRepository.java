package com.example.shoestore.core.sale.store.repository;

import com.example.shoestore.entity.OrderDetail;
import com.example.shoestore.repository.OrderDetailsRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminOrderDetailStoreRepository extends OrderDetailsRepository {

    @Query(nativeQuery = true, value = """
                    SELECT `order_detail`.`id`,
                        `order_detail`.`quantity`,
                        `order_detail`.`total_price`,
                        `order_detail`.`is_deleted`,
                        `order_detail`.`shoes_details_id`,
                        `order_detail`.`order_id`
                    FROM `database_final_datn_g33321`.`order_detail`
                    WHERE `order_detail`.`order_id` = :idOrder;
            """)
    List<OrderDetail> findByIdOrder(@Param("idOrder") Long idOrder);
}
