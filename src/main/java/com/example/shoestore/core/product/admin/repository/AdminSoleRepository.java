package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Brand;
import com.example.shoestore.entity.Sole;
import com.example.shoestore.repository.Solerepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminSoleRepository extends Solerepository {

    @Query(value = "select * from sole sl where sl.is_deleted = 0 and sl.id = :id", nativeQuery = true)
    Optional<Sole> getExistById(@Param("id") Long id);

    @Query(value = "select * from sole sl where sl.is_deleted = 0 and sl.id IN (:ids)",nativeQuery = true)
    List<Sole> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from sole sl where sl.is_deleted = 0 ", nativeQuery = true)
    List<Sole> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE sole sl SET sl.is_deleted = :isDelete WHERE sl.is_deleted = 0 AND sl.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true, value = """
                   select * from sole sl where sl.is_deleted = 0 
                      AND (
                           :codeOrName IS NULL
                           OR (
                               sl.code LIKE CONCAT('%', :codeOrName, '%')
                               OR sl.name LIKE CONCAT('%', :codeOrName, '%')
                           )
                       )
            """)
    Page<Sole> search(@Param("codeOrName") String codeOrName, Pageable pageable);
}
