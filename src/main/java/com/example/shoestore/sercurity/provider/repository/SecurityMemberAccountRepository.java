package com.example.shoestore.sercurity.provider.repository;

import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.repository.MemberAccountRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface SecurityMemberAccountRepository extends MemberAccountRepository {

    @Query(nativeQuery = true, value = "SELECT mac.* FROM member_account mac WHERE mac.userName = :username AND mac.status = 1")
    MemberAccount getMemberAccountByUsername(@Param("username") String username);

}
