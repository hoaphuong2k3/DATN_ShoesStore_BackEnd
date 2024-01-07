package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Toe;
import com.example.shoestore.repository.ToeRepository;
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
public interface AdminToeRepository extends ToeRepository {

    @Query(value = "select * from toe t where t.is_deleted = 0 and t.id = :id",nativeQuery = true)
    Optional<Toe> getExistById(@Param("id") Long id);

    @Query(value = "select * from toe t where t.is_deleted = 0 and t.id IN (:ids)",nativeQuery = true)
    List<Toe> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from toe t where t.is_deleted = 0 ",nativeQuery = true)
    List<Toe> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE toe t SET t.is_deleted = :isDelete WHERE t.is_deleted = 0 AND t.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from toe t where t.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       t.code LIKE CONCAT('%', :codeOrName, '%')
                       OR t.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Toe> search(@Param("codeOrName") String codeOrName, Pageable pageable);
}
