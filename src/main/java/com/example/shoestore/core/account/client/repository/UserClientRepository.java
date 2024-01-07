package com.example.shoestore.core.account.client.repository;

import com.example.shoestore.core.account.client.model.request.UpdateClientDTO;
import com.example.shoestore.core.account.staff.model.response.AdminAccountResponse;
import com.example.shoestore.core.account.staff.model.response.ForgotPasswordResponse;
import com.example.shoestore.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserClientRepository extends ClientRepository {


    @Query(nativeQuery = true, value = """
            SELECT  `client`.`username`,
                    `client`.`fullname`
            FROM `database_final_datn_g33321`.`client`
            WHERE `client`.`phone_number` LIKE :input OR `client`.`email` LIKE :input
             """)
    ForgotPasswordResponse findByPhonenumberOrEmail(@Param("input") String input);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`client`
                        SET `password` = :password
                        WHERE `id` = :id          
            """)
    void changePassword(@Param("password") String password, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
                        SELECT COUNT(*) 
                        FROM database_final_datn_g33321.client 
                        WHERE phone_number = :phonenumber
                        AND is_deleted =  :deleted 
            """)
    int existsByPhonenumber(@Param("phonenumber") String phonenumber, @Param("deleted") Boolean deleted);

    @Query(nativeQuery = true, value = """
                        SELECT COUNT(*) 
                        FROM database_final_datn_g33321.client 
                        WHERE username = :username
                        AND is_deleted =  :deleted 
            """)
    int existsByUsername(@Param("username") String username, @Param("deleted") Boolean deleted);


    @Query(nativeQuery = true, value = """
                        SELECT COUNT(*) 
                        FROM database_final_datn_g33321.client 
                        WHERE email = :email
                        AND is_deleted =  :deleted 
            """)
    int existsByEmail(@Param("email") String email, @Param("deleted") Boolean deleted);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`client`
                        SET `avatar` = :avatar
                        WHERE `id` = :id     
            """)
    void changeAvatar(@Param("avatar") byte[] avatar, @Param("id") Long id);


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
                            a.`fullname`,
                            a.`username`,
                            a.`gender`,
                            a.`date_of_birth` as dateOfBirth,
                            a.`email`,
                            a.`avatar`,
                            a.`total_points` AS totalPoints,
                            a.`phone_number` as phoneNumber,
                            a.`created_time` as createdTime,
                            a.`updated_time` as updatedTime,
                            a.`status`,
                            a.`is_deleted` as isDeteted
                        FROM `database_final_datn_g33321`.`client` a
                        WHERE a.`id` = :id
            """)
    AdminAccountResponse findAccount(@Param("id") Long id);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`client`
                        SET `is_deleted` = :isDelete
                        WHERE `id` = :id     
            """)
    void deleteClient(@Param("isDelete") Boolean isDelete, @Param("id") Long id);

}
