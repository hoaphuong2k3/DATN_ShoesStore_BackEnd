package com.example.shoestore.core.exchange.exchange.repository;

import com.example.shoestore.repository.ExchangeRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary
@Repository
public interface ExchangeShoesRepository extends ExchangeRepository {

    @Query(nativeQuery = true, value = """
        SELECT count(*) FROM database_final_datn_g33321.order_detail
        WHERE order_id = :idOrder AND shoes_details_id IN (:idShoesDetailList) ;
""")
    int existsByOrder(@Param("idOrder") Long idOrder, @Param("idShoesDetailList") List<Long> idShoesDetailList);

    @Query(nativeQuery = true, value = """
                         SELECT quantity FROM database_final_datn_g33321.order_detail
                        WHERE order_id = :idOrder AND shoes_details_id = :idShoesDetail ;
            """)
    int getQuantity(@Param("idOrder") Long idOrder, @Param("idShoesDetail") Long idShoesDetail);

    @Query(nativeQuery = true, value = """
            SELECT status
            FROM database_final_datn_g33321.customer_order
            WHERE id = :id
""")
    int getStatusInBill(@Param("id") Long id);


}
