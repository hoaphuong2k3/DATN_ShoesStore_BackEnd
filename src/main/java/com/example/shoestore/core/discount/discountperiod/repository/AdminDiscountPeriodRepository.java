//package com.example.shoestore.core.discount.discountperiod.repository;
//
//import com.example.shoestore.entity.Discount;
//import com.example.shoestore.entity.DiscountPeriod;
//import com.example.shoestore.repository.DiscountPeriodRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Repository
//public interface AdminDiscountPeriodRepository extends DiscountPeriodRepository {
//
//    @Query(value = "select * from discount_periods where DATE(NOW()) between discount_periods.start_date and discount_periods.end_date and status = 0 and is_deleted = 0", nativeQuery = true)
//    List<DiscountPeriod> getAll();
//
//    @Query(value = "SELECT * FROM discount_periods WHERE (code like :code OR :code IS NULL)" +
//            "and (sale_percent BETWEEN COALESCE(:min, sale_percent) AND COALESCE(:max, sale_percent))" +
//            "and (name like :name OR :name IS NULL) " +
//            "and(created_time BETWEEN COALESCE(:fromDate, created_time) AND COALESCE(:toDate, created_time)) " +
//            "and status = COALESCE(:statusSearch, status) " +
//            "and is_deleted = COALESCE(:isDeleteSearch, is_deleted)", nativeQuery = true)
//    Page<DiscountPeriod> search(@Param("code") String code, @Param("name") String name,
//                                @Param("min") Integer min, @Param("max") Integer max,
//                                @Param("fromDate") LocalDate fromDate,
//                                @Param("toDate") LocalDateTime toDate,
//                                @Param("statusSearch") Integer status,
//                                @Param("isDeleteSearch") Integer isDeleteSearch, Pageable pageable);
//
//
//    @Modifying
//    @Transactional
//    @Query(value = "update discount_periods set status = :status where id =:id", nativeQuery = true)
//    void setDiscountPeriodById(@Param("status") int status, @Param("id") Long idSetDiscountPeriod);
//
//    @Modifying
//    @Transactional
//    @Query(value = "update discount_periods set is_deleted = 1 where id =:id", nativeQuery = true)
//    void deleteDiscountPeriodById(@Param("id") Long idDeleteDiscountPeriod);
//
//    @Transactional
//    @Modifying
//    @Query(value = "update discount_periods set status = :status where id =:id", nativeQuery = true)
//    Discount restoreDiscountPeriod(@Param("status") int status, @Param("id") Long idStopVoucher);
//
//    @Query(value = "select count(*) from discount_periods where name =:name", nativeQuery = true)
//    int existsByName(@Param("name") String name);
//
//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE discounts\n" +
//            "SET end_date = DATE_ADD(end_date, INTERVAL IF(MOD(YEAR(end_date), 4) = 0, 4, 1) YEAR)\n" +
//            "WHERE id = :id", nativeQuery = true)
//    void updateYears(@Param("id") Long id);
//}
