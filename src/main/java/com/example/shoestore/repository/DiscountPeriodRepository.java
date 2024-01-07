package com.example.shoestore.repository;

import com.example.shoestore.entity.DiscountPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountPeriodRepository extends JpaRepository<DiscountPeriod, Long> {
}
