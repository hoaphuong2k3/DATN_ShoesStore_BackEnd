package com.example.shoestore.sercurity.provider.repository;

import com.example.shoestore.repository.RolesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRoleRepository extends RolesRepository {

    @Query(nativeQuery = true, value = "SELECT rl.role_name FROM role rl WHERE rl.id = :roleId")
    Optional<String> getRoleNameByRoleId(@Param("roleId") Long roleId);
}
