package com.example.shoestore.core.sale.cart.repository;

import com.example.shoestore.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("cartClientRepository")
public interface UserClientRepository extends ClientRepository {

    @Query(nativeQuery = true, value = """
            SELECT  `client`.`total_points`
            FROM `database_final_datn_g33321`.`client`
            WHERE `client`.`id` = :id
            """)
    Integer getTotalPoints(@Param("id") Long idClient);

}
