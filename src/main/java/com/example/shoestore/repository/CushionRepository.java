package com.example.shoestore.repository;

import com.example.shoestore.entity.Cushion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CushionRepository extends JpaRepository<Cushion, Long> {

}
