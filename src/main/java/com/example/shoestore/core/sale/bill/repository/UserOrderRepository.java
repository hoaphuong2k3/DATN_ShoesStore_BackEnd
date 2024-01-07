package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.core.sale.bill.model.response.CartResponse;
import com.example.shoestore.core.sale.bill.model.response.DeliveryResponse;
import com.example.shoestore.core.sale.bill.model.response.StatusShoesDetail;
import com.example.shoestore.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends OrdersRepository {


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`customer_order`
            SET `status` = :status
            WHERE `id` = :id
            """)
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT `shoes_detail`.`status`,
            `shoes_detail`.`id`
            FROM `database_final_datn_g33321`.`shoes_detail`
            WHERE `shoes_detail`.`id` IN :id
                        """)
    List<StatusShoesDetail> getStatus(@Param("id") List<Long> id);

    @Query(nativeQuery = true, value = """
            SELECT  `customer_order`.`points`
            FROM `database_final_datn_g33321`.`customer_order`
            WHERE `customer_order`.`id` = :id
            """)
    Integer getPointsOrder(@Param("id") Long idOrder);


    @Query(nativeQuery = true, value = """
            SELECT `delivery_orders`.`id`,
                `delivery_orders`.`code`,
                `delivery_orders`.`ship_date` AS shipDate,
                `delivery_orders`.`cancellation_date` as cancellationDate,
                `delivery_orders`.`received_date` as receivedDate,
                `delivery_orders`.`delivery_address` as deliveryAddress,
                `delivery_orders`.`recipient_name` as recipientName,
                `delivery_orders`.`recipient_phone` as recipientPhone,
                `delivery_orders`.`delivery_cost` as deliveryCost,
                `delivery_orders`.`status`
            FROM `database_final_datn_g33321`.`delivery_orders`
            WHERE   `delivery_orders`.`order_id` = :idOrder 
            AND  `delivery_orders`.`is_deleted` = 0
            """)
    DeliveryResponse detailDelivery(@Param("idOrder") Long idOrder);

    @Query(nativeQuery = true, value = """
                SELECT
                     row_number() OVER (ORDER BY `order_detail`.`id`) AS stt,
                    `order_detail`.`id`,
                    `order_detail`.`quantity`,
                    `order_detail`.`total_price` AS totalPrice,
                    `order_detail`.`price`,
                    `shoes_detail`.`discount_price` AS discountPrice,
                    `size`.`name` AS sizeName,
                    `color`.`name` AS colorName,
                    `shoes`.`name` AS shoesName,
                    `shoes`.`img_uri` AS imgUri
                FROM `database_final_datn_g33321`.`order_detail`
                LEFT JOIN `database_final_datn_g33321`.`shoes_detail` ON `order_detail`.`shoes_details_id` = `shoes_detail`.`id`
                LEFT JOIN `database_final_datn_g33321`.`size` ON `shoes_detail`.`size_id` = `size`.`id`
                LEFT JOIN `database_final_datn_g33321`.`color` ON `shoes_detail`.`color_id` = `color`.`id`
                LEFT JOIN `database_final_datn_g33321`.`shoes` ON `shoes_detail`.`shoes_id` = `shoes`.`id`
                WHERE  `order_detail`.`order_id` = :idOrder
                ORDER BY `order_detail`.`id` ASC
            """)
    List<CartResponse> listCart(@Param("idOrder") Long idOrder);


}
