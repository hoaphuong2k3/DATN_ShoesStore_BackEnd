package com.example.shoestore.sercurity.provider.repository;

import com.example.shoestore.repository.AccountRolesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SecurityAccountRolesRepository extends AccountRolesRepository {

    @Query(nativeQuery = true,value = """
            SELECT r.role_name
            FROM member_account_role macr
            LEFT JOIN member_account mac ON macr.id_member_account =  mac.id
            LEFT JOIN role r ON macr.id_role = r.id
            WHERE mac.username = :username
             """)
    Set<String> getRoleNameUserByUsername(@Param(("username")) String username);
}
