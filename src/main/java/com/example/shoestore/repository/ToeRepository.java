package com.example.shoestore.repository;

import com.example.shoestore.entity.Toe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToeRepository extends JpaRepository<Toe,Long> {
}
