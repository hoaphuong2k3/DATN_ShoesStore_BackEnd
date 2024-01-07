package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.core.sale.bill.model.response.*;
import com.example.shoestore.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface AdminOrderRepository extends OrdersRepository {
    @Query(nativeQuery = true, value = """
            SELECT
                               co.`id`,
                               co.`client_id` AS clientId,
                               co.`code`,
                               co.`total_money` AS totalMoney,
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
                               c.`fullname` AS fullnameCLient,
                               c.`phone_number` AS phoneNumber,
                               ma.`fullname` AS fullnameStaff,
                               ma.`username` ,
                               fe.`name` AS nameFreeGift,
                               fe.`image`AS imageFreeGift
                           FROM
                               `database_final_datn_g33321`.`customer_order` AS co
                           LEFT JOIN
                               `database_final_datn_g33321`.`discounts` AS d ON co.`discount_id` = d.`id`
                           LEFT JOIN
                               `database_final_datn_g33321`.`discount_periods` AS dp ON co.`discount_periods_id` = dp.`id`
                           LEFT JOIN
                               `database_final_datn_g33321`.`free_gift` AS fe ON dp.`gift_id` = fe.`id`
                           LEFT JOIN
                               `database_final_datn_g33321`.`client` AS c ON co.`client_id` = c.`id`
                           LEFT JOIN
            				`database_final_datn_g33321`.`member_account` AS ma ON co.`member_account_id` = ma.`id`
                           WHERE
                               co.`is_deleted` = :isDeleted
                               AND co.`status` = :status
                               AND (co.`code` LIKE CONCAT('%', :codeOrder, '%')OR :codeOrder IS NULL)
                           ORDER BY co.`updated_time` DESC
                       """)
    Page<OrderStatusResponse> findByStatus(@Param("status") int status,
                                           @Param("isDeleted") Boolean isDeleted,
                                           @Param("codeOrder") String code,
                                           Pageable pageable);


    @Query(nativeQuery = true, value = """
            SELECT 
                `customer_order`.`code`,
                `member_account`.`fullname` AS nameAdmin,
                `client`.`fullname` AS nameClient,
                `customer_order`.`total_money` AS totalMoney,
                `customer_order`.`total_payment` AS totalPayment,
                `customer_order`.`payment_method` AS paymentMethod,
                `customer_order`.`date_payment` AS datePayment,
                `customer_order`.`created_time` AS createdTime,
                `customer_order`.`sale_status` AS saleStatus,
                `customer_order`.`points`,
                `customer_order`.`status`
            FROM `database_final_datn_g33321`.`customer_order`
            LEFT JOIN`database_final_datn_g33321`.`client`  ON `customer_order`.`client_id` = `client`.`id`
            LEFT JOIN`database_final_datn_g33321`.`member_account`  ON `customer_order`.`member_account_id` = `member_account`.`id`
            WHERE  (`customer_order`.`status` = :status )
            AND (`customer_order`.`created_time` = :date OR :date IS NULL)
              """)
    List<ExportOrder> findListExport(@Param("status") int status, @Param("date") LocalDate date);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`customer_order`
            SET `status` = :status , `member_account_id` = :idStaff
            WHERE `id` = :id
            """)
    void updateStatus(@Param("status") Integer status, @Param("idStaff") Long idStaff, @Param("id") Long id);


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
            AND  `delivery_orders`.`is_deleted` = :isDeleted
            """)
    DeliveryResponse detailDelivery(@Param("idOrder") Long id, @Param("isDeleted") Boolean isDeleted);

    @Query(nativeQuery = true, value = """
            SELECT
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
            """)
    List<CartResponse> listCart(@Param("idOrder") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """

            UPDATE `database_final_datn_g33321`.`order_detail`
            SET
            `quantity` = :quantity,
            `total_price` = :totalPrice
            WHERE `id` = :id
            """)
    void updateQuantity(@Param("quantity") Integer quantity, @Param("totalPrice") BigDecimal totalPrice, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`customer_order`
            SET `total_money` = :totalMoney,
                `total_Payment` = :totalPayment
            WHERE `id` = :idOrder
                        """)
    void udpateTotalMoney(@Param("totalMoney") BigDecimal totalPrice, @Param("totalPayment") BigDecimal totalPayment, @Param("idOrder") Long idOrder);


    @Query(nativeQuery = true, value = """
            SELECT count(*) 
            FROM database_final_datn_g33321.customer_order 
            WHERE status = :status 
            """)
    Integer countStatus(@Param("status") Integer status);

}
