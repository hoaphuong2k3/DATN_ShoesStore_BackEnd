package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Cushion;
import com.example.shoestore.repository.CushionRepository;
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
public interface AdminCushionRepository extends CushionRepository {

    @Query(value = "select * from cushion c where c.is_deleted = 0 and c.id = :id",nativeQuery = true)
    Optional<Cushion> getExistById(@Param("id") Long id);

    @Query(value = "select * from cushion c where c.is_deleted = 0 and c.id IN (:ids)",nativeQuery = true)
    List<Cushion> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from cushion c where c.is_deleted = 0 ",nativeQuery = true)
    List<Cushion> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE cushion c SET c.is_deleted = :isDelete WHERE c.is_deleted = 0 AND c.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from cushion c where c.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       c.code LIKE CONCAT('%', :codeOrName, '%')
                       OR c.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Cushion> search(@Param("codeOrName") String codeOrName, Pageable pageable);


}
