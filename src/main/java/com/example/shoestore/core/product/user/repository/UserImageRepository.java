package com.example.shoestore.core.product.user.repository;

import com.example.shoestore.entity.Image;
import com.example.shoestore.repository.ImageRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserImageRepository extends ImageRepository {

    @Query(nativeQuery = true, value = """
            SELECT * FROM image im where im.is_deleted = 0 AND im.shoes_detail_id = :id 
            """)
    List<Image> getExistByShoesDetailId(@Param("id") Long id);

}
