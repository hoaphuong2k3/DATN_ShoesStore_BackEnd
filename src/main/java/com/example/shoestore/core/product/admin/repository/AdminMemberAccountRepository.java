package com.example.shoestore.core.product.admin.repository;

import com.example.shoestore.repository.MemberAccountRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMemberAccountRepository extends MemberAccountRepository {

    @Query(nativeQuery = true, value = """
            select mbac.email from member_account mbac
            INNER JOIN member_account_role mbacr ON mbac.id = mbacr.id_member_account
            WHERE mbacr.id_role = 1 AND mbac.status = 1 AND mbac.is_deleted = 0
            """ )
    List<String> getEmailAdmin();
}
