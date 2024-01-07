package com.example.shoestore.core.sale.cart.repository;

import com.example.shoestore.entity.Image;
import com.example.shoestore.repository.ImageRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageCartRepository extends ImageRepository {

    @Query(nativeQuery = true, value = """
            SELECT 
            `image`.`id`,
            `image`.`image_uri`,
            `image`.`image_name`,
            `image`.`created_by`,
            `image`.`updated_by`,
            `image`.`created_time`,
            `image`.`updated_time`,
            `image`.`is_deleted`,
            `image`.`shoes_detail_id`
            FROM `database_final_datn_g33321`.`image`
            WHERE `image`.`shoes_detail_id` = :idShoes
                       """)
    List<Image> listImage(@Param("idShoes") Long idShoes);
}
