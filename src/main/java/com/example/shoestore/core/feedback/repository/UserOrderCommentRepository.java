package com.example.shoestore.core.feedback.repository;

import com.example.shoestore.core.statistics.dto.ShoeDetailInfo;
import com.example.shoestore.repository.OrdersRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderCommentRepository extends OrdersRepository {

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
                             LEFT JOIN customer_order co ON co.id = od.order_id
                             LEFT JOIN client cl ON cl.id = co.client_id
                             WHERE sh.is_deleted = 0 and cl.id = :clientId
                             GROUP BY sh.name, c.name , s.name
                             HAVING 1 = 1
            """)
    List<ShoeDetailInfo> getDetailShoe(@Param("clientId") Long clientId);

}
