package com.example.shoestore.core.discount.invoiceproduct.repository;

import com.example.shoestore.core.discount.invoiceproduct.discountDto.response.DiscountProjection;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.repository.DiscountRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminDiscountRepository extends DiscountRepository {

    @Query(value = "SELECT * FROM discounts WHERE (code like :code OR :code IS NULL) and" +
            "(name like :name OR :name IS NULL) " +
            "and (min_price BETWEEN COALESCE(:min, min_price) AND COALESCE(:max, min_price))" +
            "and (start_date BETWEEN COALESCE(:fromDate, start_date) AND COALESCE(:toDate, start_date)) " +
            "and status = COALESCE(:statusSearch, status) " +
            "and discount_type = COALESCE(:discountTypeSearch, discount_type)" +
            "and is_deleted = COALESCE(:isDeleteSearch, is_deleted)" +
            "order by discounts.created_time DESC"
            ,nativeQuery = true)
    Page<Discount> searchVoucher(@Param("code") String code, @Param("name") String name,
                          @Param("min") Integer min, @Param("max") Integer max,
                          @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDateTime toDate,
                          @Param("statusSearch") Integer status,
                          @Param("discountTypeSearch") Integer discountTypeSearch,
                          @Param("isDeleteSearch") Integer isDeleteSearch, Pageable pageable);

    @Query(value = "SELECT d.id as id, d.code as code, d.name as name, d.sale_price as salePrice, " +
            "d.sale_percent as salePercent, d.quantity as quantity, d.min_price as minPrice, d.description as description, " +
            "d.start_date as startDate, d.end_date as endDate, d.created_by as createdBy, d.updated_by updatedBy, " +
            "d.created_time as createdTime, d.updated_time as updatedTime, d.status as status, d.is_deleted as isDeleted, " +
            "d.discount_type as discountType " +
            "FROM discounts d " +
            "LEFT JOIN discounts_shoes_detail dsd ON d.id = dsd.promo_id WHERE (d.code like :code OR :code IS NULL) and " +
            "(d.name like :name OR :name IS NULL) " +
            "and (d.min_price BETWEEN COALESCE(:min, d.min_price) AND COALESCE(:max, d.min_price)) " +
            "and (d.start_date BETWEEN COALESCE(:fromDate, d.start_date) AND COALESCE(:toDate, d.start_date)) " +
            "and d.status = COALESCE(:statusSearch, d.status) " +
            "and d.discount_type = COALESCE(:discountTypeSearch, d.discount_type) " +
            "and d.is_deleted = COALESCE(:isDeleteSearch, d.is_deleted) ",nativeQuery = true)
    Page<DiscountProjection> searchPromo(@Param("code") String code, @Param("name") String name,
                                         @Param("min") Integer min, @Param("max") Integer max,
                                         @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDateTime toDate,
                                         @Param("statusSearch") Integer status,
                                         @Param("discountTypeSearch") Integer discountTypeSearch,
                                         @Param("isDeleteSearch") Integer isDeleteSearch, Pageable pageable);

    @Query(value = """
    select * from discounts d where NOW() between d.start_date and d.end_date and d.discount_type = 0 and d.status = 0 and d.available = 0 and
        NOT EXISTS (
        SELECT 1
        FROM customer_order co
                    WHERE co.discount_id = d.id
        AND co.client_id = :clientId
        )
        LIMIT 0, 1000
    """,nativeQuery = true)
    List<Discount> getAllIsActiveVoucher(@Param("clientId") Long client);

    @Query(value = "select * from discounts where NOW() between discounts.start_date and discounts.end_date and discount_type = 1 and status = 0",nativeQuery = true)
    List<Discount> getAllIsActivePromo();

    @Query(value = "select * from discounts where discount_type = :type and status = :status and is_deleted = :isdelete",nativeQuery = true)
    List<Discount> getAllBy(@Param("type") int discount_type,@Param("status") int status,@Param("isdelete") int isdelete);

    @Transactional
    @Modifying
    @Query(value = "update discounts set status = 2 where id =:id",nativeQuery = true)
    void stopVoucher(@Param("id") Long idStopVoucher);


    @Query(value = "select * from discounts where id =:id",nativeQuery = true)
    Discount findByIdDiscount(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "update discounts set status = :status where id =:id",nativeQuery = true)
    Discount restoreVoucher(@Param("status") int status,@Param("id") Long idStopVoucher);

    @Query(value = "select count(*) from discounts where code =:code",nativeQuery = true)
    int existsByCode(@Param("code") String code);

    @Query(value = "select count(*) from discounts where name =:name and is_deleted = 0",nativeQuery = true)
    int existsByName(@Param("name") String name);

    @Query(value = "select count(*) from discounts where status = 0 and id =:id",nativeQuery = true)
    int existsVoucher(@Param("id") Long updateVoucher);

    @Modifying
    @Transactional
    @Query(value = "update discounts set is_deleted = 1 where id =:id",nativeQuery = true)
    void deleteDiscountById(@Param("id") Long idDeleteVoucher);

    @Modifying
    @Transactional
    @Query(value = "update discounts set status = 2 where id =:id",nativeQuery = true)
    void stopDiscountById(@Param("id") Long idDeleteVoucher);

    @Modifying
    @Transactional
    @Query(value = "update discounts set start_date = NOW(),is_deleted = 0,status = 0 where id =:id",nativeQuery = true)
    void setDiscountById(@Param("id") Long idSetVoucher);

    @Modifying
    @Transactional
    @Query(value = "update discounts v set v.status = 0 where id = :id",nativeQuery = true)
    void updateDiscount(@Param("id") long id);
}
