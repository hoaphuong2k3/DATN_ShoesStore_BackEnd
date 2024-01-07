package com.example.shoestore.core.discount.freegift.repository;

import com.example.shoestore.core.discount.freegift.model.response.FreeGiftResponse;
import com.example.shoestore.repository.FreeGiftRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFreeGiftRepository extends FreeGiftRepository {


    @Query(nativeQuery = true, value = """
            SELECT `free_gift`.`id`,
                `free_gift`.`code`,
                `free_gift`.`name`,
                `free_gift`.`image`,
                `free_gift`.`quantity`,
                `free_gift`.`created_time` as createdTime,
                `free_gift`.`updated_time` as updatedTime,
                `free_gift`.`created_by` as createdBy,
                `free_gift`.`updated_by` as updatedBy,
                `free_gift`.`status`,
                `free_gift`.`is_deleted` as isDeleted
            FROM `database_final_datn_g33321`.`free_gift`
            WHERE `free_gift`.`is_deleted` = :isDeleted
            """)
    Page<FreeGiftResponse> pageFreeGift(@Param("isDeleted") Boolean isDeleted, Pageable pageable);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`free_gift`
            SET `image` = :image
            WHERE `id` = :id
            """)
    void createImage(@Param("image") byte[] image, Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE `database_final_datn_g33321`.`free_gift`
            SET `is_deleted` = :isDeleted
            WHERE `id` = :id
            """)
    void delete(@Param("isDeleted") Boolean image, Long id);
}
