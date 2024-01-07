package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.account.staff.model.request.CreateAdminDTO;
import com.example.shoestore.core.account.staff.model.response.SendEmail;
import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.account.staff.model.request.RegisterAccountDTO;
import com.example.shoestore.core.account.staff.model.request.UpdateAccountDTO;
import com.example.shoestore.core.account.staff.model.response.AccountResponse;
import com.example.shoestore.entity.Client;
import com.example.shoestore.entity.MemberAccount;
import org.springframework.data.domain.Page;


public interface AccountMapper extends BaseMapper<MemberAccount, AccountResponse> {

    MemberAccount registerDTOToEntity(RegisterAccountDTO accountDTO);

    MemberAccount updateDTOToEntity(UpdateAccountDTO accountDTO);

    MemberAccount adminDTOToEntity(CreateAdminDTO adminDTO);

    SendEmail sendEmailEntityToDTO(MemberAccount memberAccount, String password);

    SendEmail sendEmailClientEntityToDTO(Client memberAccount, String password);
    Page<AccountResponse> pageEntityTODTO(Page<MemberAccount> pageAccount);

}
