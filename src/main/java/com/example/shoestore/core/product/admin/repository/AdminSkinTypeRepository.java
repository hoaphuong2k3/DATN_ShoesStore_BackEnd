package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.entity.SkinType;
import com.example.shoestore.repository.SkinTypeRepository;
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
public interface AdminSkinTypeRepository extends SkinTypeRepository {

    @Query(value = "select * from skin_type st where st.is_deleted = 0 and st.id = :id",nativeQuery = true)
    Optional<SkinType> getExistById(@Param("id") Long id);

    @Query(value = "select * from skin_type st where st.is_deleted = 0 and st.id IN (:ids)",nativeQuery = true)
    List<SkinType> getExistByIds(@Param("ids") List<Long> ids);

    @Query(value = "select * from skin_type st where st.is_deleted = 0 ",nativeQuery = true)
    List<SkinType> getAll();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE skin_type st SET st.is_deleted = :isDelete WHERE st.is_deleted = 0 AND st.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Query(nativeQuery = true,value = """
           select * from skin_type st where st.is_deleted = 0 
              AND (
                   :codeOrName IS NULL
                   OR (
                       st.code LIKE CONCAT('%', :codeOrName, '%')
                       OR st.name LIKE CONCAT('%', :codeOrName, '%')
                   )
               )
    """)
    Page<SkinType> search(@Param("codeOrName") String codeOrName, Pageable pageable);
}
