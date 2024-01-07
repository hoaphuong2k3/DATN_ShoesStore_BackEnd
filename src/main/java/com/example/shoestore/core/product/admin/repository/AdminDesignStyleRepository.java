package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.DesignStyle;
import com.example.shoestore.repository.DesignStyleRepository;
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
public interface AdminDesignStyleRepository extends DesignStyleRepository {

    @Query(value = "select * from design_style ds where ds.is_deleted = 0 and ds.id = :id",nativeQuery = true)
    Optional<DesignStyle> getExistById(@Param("id") Long id);

    @Query(value = "select * from design_style ds where ds.is_deleted = 0 and ds.id IN (:ids)",nativeQuery = true)
    List<DesignStyle> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from design_style ds where ds.is_deleted = 0 ",nativeQuery = true)
    List<DesignStyle> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE design_style ds SET ds.is_deleted = :isDelete WHERE ds.is_deleted = 0 AND ds.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from design_style ds where ds.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       ds.code LIKE CONCAT('%', :codeOrName, '%')
                       OR ds.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<DesignStyle> search(@Param("codeOrName") String codeOrName, Pageable pageable);

}
