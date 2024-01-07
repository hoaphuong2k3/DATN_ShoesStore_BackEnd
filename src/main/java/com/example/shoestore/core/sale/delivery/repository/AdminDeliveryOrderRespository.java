package com.example.shoestore.core.sale.delivery.repository;

import com.example.shoestore.core.sale.delivery.model.response.DeliveryOrderResponse;
import com.example.shoestore.repository.DeliveryOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Repository
public interface AdminDeliveryOrderRespository extends DeliveryOrderRepository {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`delivery_orders`
            SET `status` = :status
            WHERE `id` = :id
            """)
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`delivery_orders`
            SET `ship_date` = :shipDate
            WHERE `id` = :id
            """)
    void updateShipDate(@Param("shipDate") LocalDateTime shipDate, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`delivery_orders`
            SET `received_date` = :receivedDate
            WHERE `id` = :id
            """)
    void updateReceivedDate(@Param("receivedDate") LocalDateTime receivedDate, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`delivery_orders`
            SET `cancellation_date` = :cancellationDate
            WHERE `id` = :id
            """)
    void updateCancellationDate(@Param("cancellationDate") LocalDateTime cancellationDate, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT
                delivery_orders.id,
                delivery_orders.code as codeDelivery,
                delivery_orders.ship_date as shipDate,
                delivery_orders.cancellation_date as cancellationDate,
                delivery_orders.received_date as receivedDate,
                delivery_orders.delivery_address as deliveryAddress,
                delivery_orders.recipient_name as recipientName,
                delivery_orders.recipient_phone as recipientPhone,
                delivery_orders.delivery_cost as deliveryCost,
                delivery_orders.created_time as createdTime,
                delivery_orders.updated_time as updatedTime,
                delivery_orders.status ,
                delivery_orders.is_deleted as isdeleted,
                delivery_orders.created_by as createBy,
                delivery_orders.updated_by as updatedBy,
                delivery_orders.order_id as orderId,
                customer_order.code
            FROM
                database_final_datn_g33321.delivery_orders
            LEFT JOIN
                database_final_datn_g33321.customer_order ON delivery_orders.order_id = customer_order.id
            WHERE delivery_orders.is_deleted = :isDelete
             AND ( delivery_orders.status LIKE :status OR :status IS NULL)
             AND ( delivery_orders.code LIKE CONCAT('%', :code, '%') OR :code IS NULL)
             AND ( delivery_orders.created_time = :createdTime OR :createdTime IS NULL)
            """)
    Page<DeliveryOrderResponse> pageDelivery(@Param("isDelete") Boolean isDelete, @Param("status") Integer status, @Param("code") String code, @Param("createdTime") LocalDate createdTime, Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT
                delivery_orders.id,
                delivery_orders.code,
                delivery_orders.ship_date as shipDate,
                delivery_orders.delivery_address as deliveryAddress,
                delivery_orders.recipient_name as recipientName,
                delivery_orders.recipient_phone as recipientPhone,
                delivery_orders.delivery_cost as deliveryCost,
                delivery_orders.created_time as createdTime,
                delivery_orders.updated_time as updatedTime,
                delivery_orders.status ,
                delivery_orders.is_deleted as isdeleted,
                delivery_orders.created_by as createBy,
                delivery_orders.updated_by as updatedBy,
                delivery_orders.order_id as orderId,
                customer_order.code
            FROM
                database_final_datn_g33321.delivery_orders
            LEFT JOIN
                database_final_datn_g33321.customer_order ON delivery_orders.order_id = customer_order.id
            WHERE delivery_orders.id = :id
            """)
    DeliveryOrderResponse findByIdDelivery(@Param("id") Long id);
}
