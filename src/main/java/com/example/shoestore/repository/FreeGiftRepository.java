package com.example.shoestore.repository;

import com.example.shoestore.entity.FreeGift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeGiftRepository extends JpaRepository<FreeGift, Long> {
}
