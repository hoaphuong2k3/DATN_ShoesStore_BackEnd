package com.example.shoestore.repository;

import com.example.shoestore.entity.ShoesExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesExchangeRepository extends JpaRepository<ShoesExchange, Long> {
}
