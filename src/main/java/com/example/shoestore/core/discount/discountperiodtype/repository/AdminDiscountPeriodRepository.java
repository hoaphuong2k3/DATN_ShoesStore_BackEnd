package com.example.shoestore.core.discount.discountperiodtype.repository;

import com.example.shoestore.core.discount.discountperiodtype.dto.response.DiscountPeriodResponsePrejection;
import com.example.shoestore.entity.Discount;
import com.example.shoestore.entity.DiscountPeriod;
import com.example.shoestore.repository.DiscountPeriodRepository;
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
public interface AdminDiscountPeriodRepository extends DiscountPeriodRepository {

    @Query(value = " SELECT d.id as id, " +
            "    d.code as code, " +
            "    d.name as name, " +
            "    d.created_by as createdBy, " +
            "    d.updated_by as updatedBy, " +
            "    d.start_date as startDate, " +
            "    d.end_date as endDate, " +
            "    d.min_price as minPrice, " +
            "    d.sale_percent as salePercent, " +
            "    d.created_time as createdTime, " +
            "    d.updated_time as updatedTime, " +
            "    d.status as status, " +
            "    d.is_deleted as isDeleted, " +
            "    d.gift_id as giftId, " +
            "    d.type_period as typePeriod, " +
            "    f.image as image " +
            "FROM discount_periods d left join free_gift f on d.gift_id = f.id " +
            "where (DATE(NOW()) between d.start_date and d.end_date) and d.status = 0 " +
            "and d.is_deleted = 0 ", nativeQuery = true)
    List<DiscountPeriodResponsePrejection> getAll();

    @Query(value = "select * from discount_periods where status = 1 and is_deleted = 0", nativeQuery = true)
    List<DiscountPeriod> getAllBe();

    @Query(value = "select * from discount_periods where (DATE(NOW()) between discount_periods.start_date and discount_periods.end_date) and status = 0 and is_deleted = 0 ", nativeQuery = true)
    List<DiscountPeriod> getAllIsRunning();

    @Query(value = """
    select * from discount_periods where  status = 1 and is_deleted = 0 and
            start_date between COALESCE(:fromDate, start_date) and COALESCE(:toDate, start_date) or
            end_date between COALESCE(:fromDate, end_date) and COALESCE(:toDate, end_date) and is_deleted = 0
            """, nativeQuery = true)
    Integer getAllIsWaiting(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = "select * from discount_periods where (DATE(NOW()) between discount_periods.start_date and discount_periods.end_date) and status = 0 and is_deleted = 0 and id not in (:id)", nativeQuery = true)
    List<DiscountPeriod> getAllIsRunningUp(@Param("id") Long id);

    @Query(value = """ 
            select * from discount_periods where status = 1 and is_deleted = 0 and id not in (:id) and (
            start_date between COALESCE(:fromDate, start_date) and COALESCE(:toDate, start_date) or
            end_date between COALESCE(:fromDate, end_date) and COALESCE(:toDate, end_date)) and is_deleted = 0 
            """, nativeQuery = true)
    Integer getAllIsWaitingUp(@Param("id") Long id,@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query(value = " SELECT d.id as id, " +
            "    d.code as code, " +
            "    d.name as name, " +
            "    d.created_by as createdBy, " +
            "    d.updated_by as updatedBy, " +
            "    d.start_date as startDate, " +
            "    d.end_date as endDate, " +
            "    d.min_price as minPrice, " +
            "    d.sale_percent as salePercent, " +
            "    d.created_time as createdTime, " +
            "    d.updated_time as updatedTime, " +
            "    d.status as status, " +
            "    d.is_deleted as isDeleted, " +
            "    d.gift_id as giftId, " +
            "    d.type_period as typePeriod, " +
            "    f.name as nameImage " +
            "FROM discount_periods d left join free_gift f on d.gift_id = f.id " +
            "WHERE (d.code like :code OR :code IS NULL)" +
            "and (d.sale_percent BETWEEN COALESCE(:min, d.sale_percent) AND COALESCE(:max, d.sale_percent))" +
            "and (d.name like :name OR :name IS NULL) " +
            "and (d.start_date BETWEEN COALESCE(:fromDate, d.start_date) AND COALESCE(:toDate, d.start_date)) " +
            "and d.status = COALESCE(:statusSearch, d.status) " +
            "and d.type_period = COALESCE(:typePeriod, d.type_period)" +
            "and d.is_deleted = COALESCE(:isDeleteSearch, d.is_deleted)" +
            "order by d.created_time DESC"
            , nativeQuery = true)
    Page<DiscountPeriodResponsePrejection> search(@Param("code") String code, @Param("name") String name,
                                      @Param("min") Integer min, @Param("max") Integer max,
                                      @Param("fromDate") LocalDate fromDate,
                                      @Param("toDate") LocalDateTime toDate,
                                      @Param("statusSearch") Integer status,
                                      @Param("isDeleteSearch") Integer isDeleteSearch,
                                      @Param("typePeriod") Integer typePeriod,
                                      Pageable pageable);


    @Modifying
    @Transactional
    @Query(value = "update discount_periods set status = :status where id =:id ", nativeQuery = true)
    void setDiscountPeriodById(@Param("status") int status, @Param("id") Long idSetDiscountPeriod);

    @Modifying
    @Transactional
    @Query(value = "update discount_periods set is_deleted = 1 where id =:id", nativeQuery = true)
    void deleteDiscountPeriodById(@Param("id") Long idDeleteDiscountPeriod);

    @Transactional
    @Modifying
    @Query(value = "update discount_periods set status = :status where id =:id and is_deleted = 0", nativeQuery = true)
    Discount restoreDiscountPeriod(@Param("status") int status, @Param("id") Long idStopVoucher);

    @Query(value = "select count(*) from discount_periods where name =:name", nativeQuery = true)
    int existsByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE discounts " +
            "SET status = 2, end_date = DATE_ADD(end_date, INTERVAL IF(MOD(YEAR(end_date), 4) = 0, 4, 1) YEAR)\n" +
            "WHERE id = :id and is_deleted = 0", nativeQuery = true)
    void updateYears(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "update discount_periods set start_date = :start_date,status = 0  where id =:id",nativeQuery = true)
    void setDiscountPeriodRunNowById(@Param("start_date") LocalDate date, @Param("id") Long idSetVoucher);
}
