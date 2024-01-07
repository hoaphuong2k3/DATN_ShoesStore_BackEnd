package com.example.shoestore.core.common;

import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.sercurity.provider.repository.SecurityMemberAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseService {
    private static SecurityMemberAccountRepository accountRepository;

    @Autowired
    private void setUserRepository(SecurityMemberAccountRepository accountRepository) {
        BaseService.accountRepository = accountRepository;
    }

    public static MemberAccount getUserLogin(){
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        MemberAccount memberAccount = accountRepository.getMemberAccountByUsername(loggedInUser.getName());
        return memberAccount;
    }

    public static PageRequest createPageRequest(int page, int size, String sortField, String sortOrder) {
        Sort sort = Sort.unsorted();

        if (sortField != null && !sortField.isEmpty()) {
            sort = Sort.by(
                    sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sortField
            );
        }

        return PageRequest.of(page, size, sort);
    }

}
