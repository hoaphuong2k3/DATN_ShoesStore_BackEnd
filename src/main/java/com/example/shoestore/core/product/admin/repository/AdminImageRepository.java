package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Image;
import com.example.shoestore.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminImageRepository extends ImageRepository {

    @Query(nativeQuery = true, value = """
            SELECT * FROM image im where im.is_deleted = 0 AND im.shoes_detail_id = :id 
            """)
    List<Image> getExistByShoesDetailId(@Param("id") Long id);

    @Query(nativeQuery = true,value = """
            select * from image im where im.is_deleted = 0 and im.id = :id
            """)
    Optional<Image> getExistById(@Param("id") Long id);

    @Query(nativeQuery = true,value = """
            select * from image im where im.is_deleted = 0 and im.shoes_detail_id = :shoesDetailId
            """)
    List<Image> getAll(@Param("shoesDetailId") Long shoesDetailId);

    @Query(nativeQuery = true,value = """
            select * from image im where im.is_deleted = 0 and im.shoes_detail_id IN (:shoesDetailIds)
            """)
    List<Image> getAllByIds(@Param("shoesDetailIds") List<Long> shoesDetailIds);

    @Query(nativeQuery = true,value = """
            select COUNT(*) from image im where im.is_deleted = 0 and im.id IN (:ids)
            """)
    long countExistByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE image im SET im.is_deleted = :isDelete WHERE im.id IN (:ids) 
            """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

}
