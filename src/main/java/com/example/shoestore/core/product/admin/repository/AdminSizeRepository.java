package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Size;
import com.example.shoestore.repository.SizeRepository;
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
public interface AdminSizeRepository extends SizeRepository {

    @Query(value = "select * from size sz where sz.is_deleted = 0 and sz.id = :id",nativeQuery = true)
    Optional<Size> getExistById(@Param("id") Long id);

    @Query(value = "select * from size sz where sz.is_deleted = 0 and sz.id IN (:ids)",nativeQuery = true)
    List<Size> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from size sz where sz.is_deleted = 0 ",nativeQuery = true)
    List<Size> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE size sz SET sz.is_deleted = :isDelete WHERE sz.is_deleted = 0 AND sz.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from size sz where sz.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       sz.code LIKE CONCAT('%', :codeOrName, '%')
                       OR sz.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Size> search(@Param("codeOrName") String codeOrName, Pageable pageable);

}
