package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.core.product.admin.repository.custom.AdminShoesDetailRepositotyCustom;
import com.example.shoestore.entity.ShoesDetail;
import com.example.shoestore.repository.ShoesDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminShoesDetailRepositoty extends ShoesDetailsRepository, AdminShoesDetailRepositotyCustom {

    @Query(nativeQuery = true, value = """
            SELECT sh.name FROM shoes sh 
            WHERE sh.id IN (:shoesIds) AND sh.id IN (SELECT shdt.shoes_id FROM shoes_detail shdt WHERE shdt.is_deleted = 0) 
            """)
    List<String> getNameByShoesIds(@Param("shoesIds") List<Long> shoesIds);

    @Query(nativeQuery = true, value = """
            SELECT cl.name FROM color cl
            WHERE cl.id IN (:colorIds) AND cl.id IN (SELECT shdt.color_id FROM shoes_detail shdt WHERE shdt.is_deleted = 0) 
            """)
    List<String> getNameByColorIds(@Param("colorIds") List<Long> colorIds);

    @Query(nativeQuery = true, value = """
            SELECT sz.name FROM size sz
            WHERE sz.id IN (:sizeIds) AND sz.id IN (SELECT shdt.size_id FROM shoes_detail shdt WHERE shdt.is_deleted = 0) 
            """)
    List<String> getNameBySizeIds(@Param("sizeIds") List<Long> sizeIds);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes_detail shdt WHERE shdt.is_deleted = 0 AND shdt.id = :id """)
    Optional<ShoesDetail> getExistById(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes_detail shdt WHERE shdt.is_deleted = 0 AND shdt.qr_code_uri = :qrCode """)
    Optional<ShoesDetail> getExistByQRCode(@Param("qrCode") String qrCode);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes_detail shdt WHERE shdt.shoes_id = :shoesId
            AND shdt.color_id = :colorId AND shdt.size_id = :sizeId AND shdt.is_deleted = 0
            """)
    ShoesDetail getExistByListIdForCreate(@Param("shoesId") Long shoesId, @Param("colorId") Long color, @Param("sizeId") Long size);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes_detail shdt WHERE shdt.id <> :id AND shdt.shoes_id = :shoesId
            AND shdt.color_id = :colorId AND shdt.size_id = :sizeId AND shdt.is_deleted = 0
            """)
    ShoesDetail getExistByListIdForUpdate(@Param("id") Long id, @Param("shoesId") Long shoesId,@Param("colorId") Long colorId, @Param("sizeId") Long sizeId);

    @Query(nativeQuery = true, value = """
            SELECT * FROM shoes_detail shdt WHERE shdt.is_deleted = 0 AND shdt.status <> 1 AND shdt.id IN (:ids) """)
    List<ShoesDetail> getNotOnBusinessByIs(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail shdt SET shdt.is_deleted = :isDelete WHERE shdt.is_deleted = 0 AND shdt.status <> 1 AND shdt.id IN (:ids) """)
    void setIsDelete(@Param("ids") List<Long> ids, @Param("isDelete") Boolean isDelete);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            UPDATE shoes_detail shdt SET shdt.status = :status WHERE shdt.id = :id """)
    void setStatus(@Param("id") Long id, @Param("status") Integer status);

}
