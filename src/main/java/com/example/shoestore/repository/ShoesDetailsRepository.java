package com.example.shoestore.repository;

import com.example.shoestore.entity.ShoesDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesDetailsRepository extends JpaRepository<ShoesDetail, Long> {


}
