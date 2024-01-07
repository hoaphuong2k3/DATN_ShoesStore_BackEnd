package com.example.shoestore.repository;

import com.example.shoestore.entity.DiscountsShoesDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountsShoesDetailRepository extends JpaRepository<DiscountsShoesDetail, Long> {
}