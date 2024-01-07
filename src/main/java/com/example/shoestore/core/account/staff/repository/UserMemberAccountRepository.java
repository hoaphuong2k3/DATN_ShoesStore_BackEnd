package com.example.shoestore.core.account.staff.repository;

import com.example.shoestore.core.account.staff.model.response.AdminAccountResponse;
import com.example.shoestore.core.account.staff.model.response.ForgotPasswordResponse;
import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.repository.MemberAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMemberAccountRepository extends MemberAccountRepository {

    @Query(nativeQuery = true, value = """
            SELECT  `member_account`.`username`,
                    `member_account`.`fullname`
            FROM `database_final_datn_g33321`.`member_account`
            WHERE `member_account`.`phone_number` LIKE :input OR `member_account`.`email` LIKE :input
             """)
    Optional<ForgotPasswordResponse> findByPhonenumberOrEmail(@Param("input") String input);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`member_account`
                        SET `password` = :password
                        WHERE `id` = :id         
            """)
    void changePassword(@Param("password") String password, @Param("id") Long id);

    @Query(nativeQuery = true, value = """
                        SELECT COUNT(*) 
                        FROM database_final_datn_g33321.member_account 
                        WHERE phone_number = :phonenumber
                         AND is_deleted = :deleted
            """)
    int existsByPhonenumber(@Param("phonenumber") String phonenumber, @Param("deleted") Boolean deleted);

    @Query(nativeQuery = true, value = """
                        SELECT COUNT(*) 
                        FROM database_final_datn_g33321.member_account 
                        WHERE username = :username
                        AND is_deleted = :deleted
            """)
    int existsByUsername(@Param("username") String username, @Param("deleted") Boolean deleted);

    @Query(nativeQuery = true, value = """
                        SELECT COUNT(*) 
                        FROM database_final_datn_g33321.member_account 
                        WHERE email = :email
                        AND is_deleted = :deleted
                        
            """)
    int existsByEmail(@Param("email") String email, @Param("deleted") Boolean deleted);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                        UPDATE `database_final_datn_g33321`.`member_account`
                        SET `avatar` = :avatar
                        WHERE `id` = :id     
            """)
    void changeAvatar(@Param("avatar") byte[] avatar, @Param("id") Long id);


    @Modifying
    @Transactional
    @Query(value = """
            UPDATE `database_final_datn_g33321`.`member_account`
            SET `fullname` = :#{#memberAccount.fullname},
                `gender` = :#{#memberAccount.gender},
                `date_of_birth` = :#{#memberAccount.dateOfBirth},
                `email` = :#{#memberAccount.email},
                `phone_number` = :#{#memberAccount.phoneNumber},
                `updated_time` = :#{#memberAccount.updatedTime},
                `address_id` = :#{#memberAccount.idAddress}
            WHERE id = :#{#memberAccount.id}
            """, nativeQuery = true)
    void updateAccount(@Param("memberAccount") MemberAccount memberAccount);

    @Query(nativeQuery = true, value = """
                        SELECT
                            a.`fullname`,
                            a.`gender`,
                            a.`date_of_birth` as dateOfBirth,
                            a.`email`,
                            a.`avatar`,
                            a.`phone_number` as phoneNumber,
                            a.`created_time` as createdTime,
                            a.`updated_time` as updatedTime,
                            a.`status`,
                            a.`is_deleted` as isDeteted,
                            adr.`provice_code` as proviceCode,
                            adr.`district_code` as districtCode,
                            adr.`commune_code` as communeCode,
                            adr.`address_detail` as addressDetail,
                        FROM `database_final_datn_g33321`.`client` a
                        LEFT JOIN `database_final_datn_g33321`.`address` adr ON a.`address_id` = adr.`id`
                        WHERE a.`id` = :id
            """)
    AdminAccountResponse findAccount(@Param("id") Long id);
}
