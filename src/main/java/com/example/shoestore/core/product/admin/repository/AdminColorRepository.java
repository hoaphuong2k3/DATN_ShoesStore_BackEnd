package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Color;
import com.example.shoestore.repository.ColorRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AdminColorRepository extends ColorRepository {

    @Query(value = "select * from color cl where cl.is_deleted = 0 and cl.id = :id",nativeQuery = true)
    Optional<Color> getExistById(@Param("id") Long id);

    @Query(value = "select * from color cl where cl.is_deleted = 0 and cl.id IN (:ids)",nativeQuery = true)
    List<Color> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from color cl where cl.is_deleted = 0 ",nativeQuery = true)
    List<Color> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE color cl SET cl.is_deleted = :isDelete WHERE cl.is_deleted = 0 AND cl.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from color cl where cl.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       cl.code LIKE CONCAT('%', :codeOrName, '%')
                       OR cl.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Color> search(@Param("codeOrName") String codeOrName, Pageable pageable);

}
