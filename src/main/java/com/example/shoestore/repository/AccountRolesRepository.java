package com.example.shoestore.repository;

import com.example.shoestore.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRolesRepository extends JpaRepository<AccountRole, Long> {
}
