package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.Brand;
import com.example.shoestore.repository.BrandRepository;
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
public interface AdminBrandsRepository extends BrandRepository {

    @Query(value = "select * from brand br where br.is_deleted = 0 and br.id = :id",nativeQuery = true)
    Optional<Brand> getExistById(@Param("id") Long id);

    @Query(value = "select * from brand br where br.is_deleted = 0 and br.id IN (:ids)",nativeQuery = true)
    List<Brand> getExistByIds(@Param("ids") List<Long> ids);
    @Query(value = "select * from brand br where br.is_deleted = 0",nativeQuery = true)
    List<Brand> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE brand br SET br.is_deleted = :isDelete WHERE br.is_deleted = 0 AND br.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from brand br where br.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       br.code LIKE CONCAT('%', :codeOrName, '%')
                       OR br.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<Brand> search(@Param("codeOrName") String codeOrName, Pageable pageable);

}
