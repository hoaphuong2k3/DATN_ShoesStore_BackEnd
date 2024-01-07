package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.core.product.admin.dto.response.ShoesFindOneResponse;
import com.example.shoestore.core.product.admin.repository.custom.AdminShoesRepositoryCustom;
import com.example.shoestore.entity.Shoes;
import com.example.shoestore.repository.ShoesRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminShoesRepository extends ShoesRepository, AdminShoesRepositoryCustom {

    @Query(nativeQuery = true,value = """
            SELECT sh.code,sh.name,
            br.id AS brandId,br.name AS brandName,
            org.id AS originId,org.name as originName,
            dsst.id AS designStyleId,dsst.name AS designStyleName,
            skt.id AS skintypeId,skt.name AS skinTypeName,
            sl.id AS soleId,sl.name AS soleName,
            ln.id AS liningId,ln.name AS liningName,
            t.id AS toeId,t.name AS toeName,
            cs.id AS cushionId,cs.name AS cushionName,
            sh.description,
            sh.img_uri as imgURI,
            sh.img_name as imgName,
            sh.created_by as createdBy,
            sh.created_time as createdTime
            from shoes sh
            INNER JOIN brand br ON sh. brand_id = br.id
            INNER JOIN origin org ON sh.origin_id = org.id
            INNER JOIN design_style dsst ON sh.design_style_id = dsst.id
            INNER JOIN skin_type skt ON sh.skin_type_id = skt.id
            INNER JOIN sole sl ON sh.sole_id = sl.id
            INNER JOIN lining ln ON sh.lining_id = ln.id
            INNER JOIN toe t ON sh.toe_id = t.id
            INNER JOIN cushion cs ON sh.cushion_id = cs.id 
            WHERE sh.is_deleted = 0 AND sh.id = :id """)
    ShoesFindOneResponse findOne(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes sh WHERE sh.is_deleted = 0 
            AND (sh.id IS NULL OR sh.id = :id OR :id IS NULL)
            AND sh.name = :name AND sh.brand_id = :brandId
            AND sh.origin_id = :originId AND sh.design_style_id = :designStyleId 
            AND sh.skin_type_id = :skinTypeId AND sh.sole_id = :soleId
            AND sh.lining_id = :liningId AND sh.toe_id = :toeId 
            AND sh.cushion_id = :cushionId  """)
    Shoes getExistByListId(@Param("id") Long id, @Param("name") String name,
                          @Param("brandId") Long brandId, @Param("originId") Long originId,
                          @Param("designStyleId") Long designStyleId, @Param("skinTypeId") Long skinTypeId,
                          @Param("soleId") Long soleId, @Param("liningId") Long liningId,
                          @Param("toeId") Long toeId, @Param("cushionId") Long cushionId);

    @Query(nativeQuery = true, value = """
            SELECT br.name FROM brand br
            WHERE br.id IN (:brandIds) AND br.id IN (SELECT sh.brand_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameByBrandIds(@Param("brandIds") List<Long> brandIds);

    @Query(nativeQuery = true, value = """
            SELECT ln.name FROM lining ln
            WHERE ln.id IN (:liningIds) AND ln.id IN (SELECT sh.lining_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameByLiningIds(@Param("liningIds") List<Long> liningIds);

    @Query(nativeQuery = true, value = """
            SELECT c.name FROM cushion c
            WHERE c.id IN (:cushionIds) AND c.id IN (SELECT sh.cushion_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameByCushionIds(@Param("cushionIds") List<Long> cushionIds);

    @Query(nativeQuery = true, value = """
            SELECT o.name FROM origin o
            WHERE o.id IN (:originIds) AND o.id IN (SELECT sh.origin_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameByOriginIds(@Param("originIds") List<Long> originIds);

    @Query(nativeQuery = true, value = """
            SELECT ds.name FROM design_style ds
            WHERE ds.id IN (:designStyleIds) AND ds.id IN (SELECT sh.design_style_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameByDesignStyleIds(@Param("designStyleIds") List<Long> designStyleIds);

    @Query(nativeQuery = true, value = """
            SELECT st.name FROM skin_type st
            WHERE st.id IN (:skinTypeIds) AND st.id IN (SELECT sh.skin_type_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameBySkinTypeIds(@Param("skinTypeIds") List<Long> skinTypeIds);

    @Query(nativeQuery = true, value = """
            SELECT sl.name FROM sole sl
            WHERE sl.id IN (:soleIds) AND sl.id IN (SELECT sh.sole_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameBySoleIds(@Param("soleIds") List<Long> soleIds);

    @Query(nativeQuery = true, value = """
            SELECT t.name FROM toe t
            WHERE t.id IN (:toeIds) AND t.id IN (SELECT sh.toe_id FROM shoes sh WHERE sh.is_deleted = 0) 
            """)
    List<String> getNameByToeIds(@Param("toeIds") List<Long> toeIds);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes sh WHERE sh.is_deleted = 0 AND sh.id = :id """)
    Optional<Shoes> getExistById(@Param("id") Long id);
    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes sh WHERE sh.is_deleted = 0 AND sh.id IN (:ids) """)
    List<Shoes> getExistByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes sh SET sh.is_deleted = :isDelete WHERE sh.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);


}
