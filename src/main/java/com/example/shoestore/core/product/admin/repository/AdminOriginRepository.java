package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Origin;
import com.example.shoestore.repository.OriginRepository;
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
public interface AdminOriginRepository extends OriginRepository {

    @Query(value = "select * from origin o where o.is_deleted = 0 and o.id = :id",nativeQuery = true)
    Optional<Origin> getExistById(@Param("id") Long id);

    @Query(value = "select * from origin o where o.is_deleted = 0 and o.id IN (:ids)",nativeQuery = true)
    List<Origin> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from origin o where o.is_deleted = 0 ",nativeQuery = true)
    List<Origin> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE origin o SET o.is_deleted = :isDelete WHERE o.is_deleted = 0 AND o.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from origin o where o.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       o.code LIKE CONCAT('%', :codeOrName, '%')
                       OR o.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Origin> search(@Param("codeOrName") String codeOrName, Pageable pageable);

}
