package com.example.shoestore.core.sale.bill.repository;

import com.example.shoestore.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("billClientRepository")
public interface UserClientRepository extends ClientRepository {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`client`
            SET `total_points` = :points
            WHERE `id` = :id
            """)
    void updatePointClient(@Param("points") Integer points,@Param("id") Long id );

    @Query(nativeQuery = true, value = """
            SELECT  `client`.`total_points`
            FROM `database_final_datn_g33321`.`client`
            WHERE `client`.`id` = :id
            """)
    Integer getTotalPoints(@Param("id") Long idClient);

}
