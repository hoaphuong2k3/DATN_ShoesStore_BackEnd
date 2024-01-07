package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.core.sale.bill.model.response.CartResponse;
import com.example.shoestore.core.sale.bill.model.response.DeliveryResponse;
import com.example.shoestore.core.sale.bill.model.response.OrderStatusResponse;
import com.example.shoestore.entity.OrderDetail;
import com.example.shoestore.repository.OrderDetailsRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderDetailRepository extends OrderDetailsRepository {

    @Query(nativeQuery = true, value = """
                    SELECT `order_detail`.`id`,
                        `order_detail`.`quantity`,
                        `order_detail`.`total_price`,
                        `order_detail`.`price`,
                        `order_detail`.`is_deleted`,
                        `order_detail`.`shoes_details_id`,
                        `order_detail`.`order_id`
                    FROM `database_final_datn_g33321`.`order_detail`
                    WHERE `order_detail`.`order_id` = :idOrder
                    AND  `order_detail`.`is_deleted` = 0
            """)
    List<OrderDetail> findByIdOrderDetail(@Param("idOrder") Long idOrder);

    @Query(nativeQuery = true, value = """
                SELECT
                    co.`id`,
                    co.`client_id` AS clientId,
                    co.`code`,
                    co.`total_payment` AS totalPayment,
                    co.`payment_method` AS paymentMethod,
                    co.`date_payment` AS datePayment,
                    co.`created_time` AS createdTime,
                    co.`updated_time` AS updatedTime,
                    co.`status`,
                    co.`created_by` AS createdBy,
                    co.`updated_by` AS updatedBy,
                    co.`is_deleted` AS isDeleted,
                    d.`sale_price` AS priceVoucher,
                    d.`sale_percent` AS percentVoucher,
                    dp.`sale_percent` AS percentPeriod,
                    c.`fullname`,
                    c.`phone_number` AS phoneNumber
                FROM
                    `database_final_datn_g33321`.`customer_order` AS co
                LEFT JOIN
                    `database_final_datn_g33321`.`discounts` AS d ON co.`discount_id` = d.`id`
                LEFT JOIN
                    `database_final_datn_g33321`.`discount_periods` AS dp ON co.`discount_periods_id` = dp.`id`
                LEFT JOIN
                    `database_final_datn_g33321`.`client` AS c ON co.`client_id` = c.`id`
                WHERE  co.`client_id` = :idClient
                    AND co.`is_deleted` = 0
                    AND co.`status` = :status
                ORDER BY  co.`created_time` DESC
            """)
    List<OrderStatusResponse> findByClient(@Param("status") int status,
                                           @Param("idClient") Long idClient);

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
    DeliveryResponse findDelivery(@Param("idOrder") Long idOrder);

    @Query(nativeQuery = true, value = """
            SELECT
                `order_detail`.`id`,
                `order_detail`.`quantity`,
                `order_detail`.`total_price` AS totalPrice,
                `order_detail`.`price`,
                `shoes_detail`.`discount_price` AS discountPrice,
                `shoes_detail`.`id` AS idShoesDetail,
                `size`.`id` AS idSize,                
                `size`.`name` AS sizeName,
                `color`.`id` AS idColor,
                `color`.`name` AS colorName,
                `shoes`.`id` AS idShoes,
                `shoes`.`name` AS shoesName,
                `shoes`.`img_uri` AS imgUri
            FROM `database_final_datn_g33321`.`order_detail`
            LEFT JOIN `database_final_datn_g33321`.`shoes_detail` ON `order_detail`.`shoes_details_id` = `shoes_detail`.`id`
            LEFT JOIN `database_final_datn_g33321`.`size` ON `shoes_detail`.`size_id` = `size`.`id`
            LEFT JOIN `database_final_datn_g33321`.`color` ON `shoes_detail`.`color_id` = `color`.`id`
            LEFT JOIN `database_final_datn_g33321`.`shoes` ON `shoes_detail`.`shoes_id` = `shoes`.`id`
            WHERE  `order_detail`.`order_id` = :idOrder
            """)
    List<CartResponse> listCart(@Param("idOrder") Long id);


}
