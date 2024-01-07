package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Lining;
import com.example.shoestore.repository.LiningRepository;
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
public interface AdminLiningRepository extends LiningRepository {

    @Query(value = "select * from lining ln where ln.is_deleted = 0 and ln.id = :id", nativeQuery = true)
    Optional<Lining> getExistById(@Param("id") Long id);

    @Query(value = "select * from lining ln where ln.is_deleted = 0 and ln.id IN (:ids)", nativeQuery = true)
    List<Lining> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from lining ln where ln.is_deleted = 0 ", nativeQuery = true)
    List<Lining> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE lining ln SET ln.is_deleted = :isDelete WHERE ln.is_deleted = 0 AND ln.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from lining ln where ln.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       ln.code LIKE CONCAT('%', :codeOrName, '%')
                       OR ln.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Lining> search(@Param("codeOrName") String codeOrName, Pageable pageable);

}

