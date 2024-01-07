package com.example.shoestore.repository;

import com.example.shoestore.entity.DesignStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignStyleRepository extends JpaRepository<DesignStyle, Long> {
}
