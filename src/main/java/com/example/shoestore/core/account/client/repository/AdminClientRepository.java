package com.example.shoestore.core.account.client.repository;

import com.example.shoestore.core.account.staff.model.response.AdminAccountResponse;
import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.entity.Client;
import com.example.shoestore.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminClientRepository extends ClientRepository {


    @Modifying
    @Transactional
    @Query(value = """
            UPDATE `database_final_datn_g33321`.`client`
            SET `fullname` = :#{#client.fullname},
                `gender` = :#{#client.gender},
                `date_of_birth` = :#{#client.dateOfBirth},
                `email` = :#{#client.email},
                `updated_time` = :#{#client.updateTime},
                `phone_number` = :#{#client.phoneNumber}
            WHERE id = :#{#client.id}
            """, nativeQuery = true)
    void updateAccount(@Param("client") UpdateClientDTO clientDTO);

    @Query(nativeQuery = true, value = """
                SELECT
                    a.`id`,
                    a.`fullname`,
                    a.`username`,
                    a.`password`,
                    a.`gender`,
                    a.`date_of_birth`,
                    a.`email`,
                    a.`avatar`,
                    a.`phone_number`,
                    a.`created_time`,
                    a.`updated_time`,
                    a.`role_id`,
                    a.`total_points`,
                    a.`status`,
                    a.`is_deleted`
                FROM `database_final_datn_g33321`.`client` a
                WHERE  a.`is_deleted` = 0
                AND (
                    (a.`fullname` LIKE CONCAT('%', :input, '%') OR :input IS NULL) OR
                    (a.`email` LIKE CONCAT('%', :input, '%') OR :input IS NULL) OR
                    (a.`phone_number` LIKE CONCAT('%', :input, '%') OR :input IS NULL)
                )
                AND (a.`gender` = :gender OR :gender IS NULL)
                AND ( a.`date_of_birth` LIKE CONCAT('%',:dateOfBirth,' %' ) OR :dateOfBirth IS NULL)
                AND (  a.`status` = :status OR :status IS NULL)
                AND (a.`created_time` LIKE CONCAT('%',:createdTime,' %' )  OR :createdTime IS NULL)
            """)
    Page<Client> pageClient(@Param("input") String input,
                            @Param("createdTime") LocalDate createdTime,
                            @Param("dateOfBirth") LocalDate dateOfBirth,
                            @Param("status") Integer status,
                            @Param("gender") Boolean gender,
                            Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT
                                        a.`username`,
                                        a.`fullname`,
                                        a.`gender`,
                                        a.`date_of_birth` as dateOfBirth,
                                        a.`email`,
                                        a.`avatar`,
                                        a.`phone_number` as phoneNumber,
                                        a.`created_time` as createdTime,
                                        a.`updated_time` as updatedTime,
                                        a.`status`,
                                        a.`is_deleted` as isDeleted
                                    FROM `database_final_datn_g33321`.`client` a
                                    WHERE a.`id` = :id
                        """)
    AdminAccountResponse findAccount(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`client`
                        SET `is_deleted` = :isDelete
                        WHERE `id` IN :id     
            """)
    void deleteClient(@Param("isDelete") Boolean isDelete, @Param("id") List<Long> id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`client`
                        SET `status` = :status
                        WHERE `id` = :id     
            """)
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

}
