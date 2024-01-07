package com.example.shoestore.core.statistics.repository;

import com.example.shoestore.core.statistics.dto.OrderInfo;

import com.example.shoestore.core.statistics.dto.Seo;
import com.example.shoestore.core.statistics.dto.ShoeDetailInfo;
import com.example.shoestore.core.statistics.dto.TopProduct;
import com.example.shoestore.repository.OrdersRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminOrdersRepository extends OrdersRepository {
    @Query(nativeQuery = true,
            value = """
                    SELECT co.code as code, co.total_money as totalMoney, co.total_payment as totalPayment,
                    co.payment_method as paymentMethod, co.date_payment as datePayment,  
                    co.shipping_price as shippingPrice, co.shipping_date as shippingDate, 
                    co.received_date as receivedDate, co.created_time as createdTime, co.updated_time as updatedTime, 
                    co.sale_status as saleStatus, co.status as status, co.created_by as createdBy,
                    co.updated_by as updatedBy, co.is_deleted as isDeleted, od.quantity as quatity, od.total_price as totalPrice, 
                    FROM customer_order co LEFT JOIN order_detail od on co.id = od.order_id
                    """)
    List<OrderInfo> getAll();

    @Query(nativeQuery = true,
            value = """
                    SELECT SUM(co.total_payment) as totalPayment, SUM(od.quantity) AS totalQuantity,
                    SUM(od.total_price) AS totalPrice, SUM(co.total_money) as totalMoney
                    FROM customer_order co
                    LEFT JOIN order_detail od ON co.id = od.order_id
                    WHERE co.created_time between COALESCE(:fromDate, co.created_time) and COALESCE(:toDate, co.created_time)
                    """)
    Seo getQuantityAndMoney(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query(nativeQuery = true,
            value = """
                        select sh.id, sh.name as name, SUM(od.quantity) AS totalQuantity FROM shoes sh
                                                                                         LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id
                                                                                         LEFT JOIN order_detail od ON shdt.id = od.shoes_details_id
                                                                                         LEFT JOIN customer_order co ON od.order_id = co.id
                                                                                         WHERE sh.is_deleted = 0 and od.order_id is not null
                                                                                         GROUP BY sh.id,sh.name AND co.created_time BETWEEN COALESCE(:fromDate, co.created_time) AND COALESCE(:toDate, co.created_time)
                                                                                         ORDER BY totalQuantity DESC
                                                                                         LIMIT 5
                    """)
    List<TopProduct> getTop5Product(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query(nativeQuery = true,
            value = """
                        select sh.id, sh.name as name, SUM(od.quantity) AS totalQuantity FROM shoes sh
                                                                                         LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id
                                                                                         LEFT JOIN order_detail od ON shdt.id = od.shoes_details_id
                                                                                         LEFT JOIN customer_order co ON od.order_id = co.id
                                                                                         WHERE sh.is_deleted = 0 and od.order_id is not null
                                                                                         GROUP BY sh.id,sh.name AND co.created_time BETWEEN COALESCE(:fromDate, co.created_time) AND COALESCE(:toDate, co.created_time)
                                                                                         ORDER BY totalQuantity ASC
                                                                                         LIMIT 5
                    """)
    List<TopProduct> getBottom5Product(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query(nativeQuery = true,
            value = """
                    SELECT DATE(co.created_time) as createdTime, SUM(co.total_payment) as totalPayment
                                       FROM customer_order co
                                       LEFT JOIN order_detail od ON co.id = od.order_id
                                       WHERE co.created_time BETWEEN COALESCE(:fromDate, co.created_time) AND COALESCE(:toDate, co.created_time)
                                       GROUP BY createdTime;
                                       
                    """)
    List<Seo> getQuantityAndMoneyByDateRangeMonth(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);


    @Query(nativeQuery = true,
            value = """
                    SELECT DATE_FORMAT(co.created_time, '%m-%Y') as createdTime, SUM(co.total_payment) as totalPayment
                                       FROM customer_order co
                                       LEFT JOIN order_detail od ON co.id = od.order_id
                                       WHERE co.created_time BETWEEN COALESCE(:fromDate, co.created_time) AND COALESCE(:toDate, co.created_time)
                                       GROUP BY createdTime;
                                       
                    """)
    List<Seo> getQuantityAndMoneyByDateRange(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query(nativeQuery = true,
            value = """
    SELECT DATE_FORMAT(co.created_time, '%d-%m-%Y') as createdTime, SUM(co.total_payment) as totalPayment,
    SUM(od.quantity) as totalQuantity
    FROM customer_order co
    LEFT JOIN order_detail od ON co.id = od.order_id
    where co.sale_status = COALESCE(:saleStatus, co.sale_status)
    and co.status = COALESCE(:status, co.status) 
    and DATE_FORMAT(co.created_time, '%d-%m-%Y') = COALESCE(DATE_FORMAT(:createdTime, '%d-%m-%Y'), co.created_time)
    group by createdTime
    """)
    List<Seo> getTotalPayment(@Param("saleStatus") Integer saleStatus, @Param("status") Integer status, @Param("createdTime") LocalDate createdTime);

    @Query(nativeQuery = true, value = """
           SELECT sh.name,c.name as color,
                  s.name as size,
                  SUM(od.quantity) as quantity FROM shoes sh
                  LEFT JOIN shoes_detail shdt ON sh.id = shdt.shoes_id
                  LEFT JOIN brand br ON br.id = sh.brand_id
                  LEFT JOIN origin o ON o.id = sh.origin_id
                  LEFT JOIN design_style dsst ON dsst.id = sh.design_style_id
                  LEFT JOIN skin_type skt ON skt.id = sh.skin_type_id
                  LEFT JOIN sole sl ON sl.id = sh.sole_id
                  LEFT JOIN lining ln ON ln.id = sh.lining_id
                  LEFT JOIN toe t ON t.id = sh.toe_id
                  LEFT JOIN cushion cs ON cs.id = sh.cushion_id
                  LEFT JOIN size s ON s.id = shdt.size_id
                  LEFT JOIN color c ON c.id = shdt.color_id
                  LEFT JOIN discounts_shoes_detail dsd ON dsd.shoes_detail_id = shdt.id
                  LEFT JOIN order_detail od ON od.shoes_details_id = shdt.id
                  WHERE sh.is_deleted = 0 and shdt.shoes_id = :shoeId 
                  GROUP BY sh.name, c.name , s.name 
                  HAVING 1 = 1
            """)
    List<ShoeDetailInfo> getDetailShoe(@Param("shoeId") Long shoeId);

}


