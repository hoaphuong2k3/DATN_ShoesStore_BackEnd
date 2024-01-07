package com.example.shoestore.sercurity.provider.dao;

import com.example.shoestore.entity.MemberAccount;
import com.example.shoestore.entity.Client;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.utils.DataUtils;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.sercurity.provider.repository.SecurityMemberAccountRepository;
import com.example.shoestore.sercurity.provider.repository.SecurityAccountRolesRepository;
import com.example.shoestore.sercurity.exception.UserDetailNotfoundException;
import com.example.shoestore.sercurity.provider.repository.SecurityClientRepository;
import com.example.shoestore.sercurity.provider.repository.SecurityRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SecurityMemberAccountRepository accountRepository;
    private final SecurityClientRepository clientRepository;
    private final SecurityRoleRepository roleRepository;
    private final SecurityAccountRolesRepository accountRolesRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        MemberAccount memberAccountByUsername = accountRepository.getMemberAccountByUsername(username);
        Client clientByUserName = clientRepository.getClientByUsername(username);

            UserDetailsCustom userDetailsCustom = new UserDetailsCustom();

        if(DataUtils.isNotNull(memberAccountByUsername)){

            Set<String> setRole = accountRolesRepository.getRoleNameUserByUsername(username);

            if(DataUtils.isEmpty(setRole)){
                throw new UserDetailNotfoundException(MessageCode.Accounts.ROLES_NOT_FOUND);
            }

            Set<SimpleGrantedAuthority> authorities = setRole.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            userDetailsCustom.setUsername(memberAccountByUsername.getUsername());
            userDetailsCustom.setPassword(memberAccountByUsername.getPassword());
            userDetailsCustom.setAuthorities(authorities);
            userDetailsCustom.setUserId(memberAccountByUsername.getId());
            return userDetailsCustom;
        }
        else if(DataUtils.isNotNull(clientByUserName)){
            String roleClient = roleRepository.getRoleNameByRoleId(clientByUserName.getIdRole()).orElseThrow(()
                    -> new UserDetailNotfoundException(MessageUtils.getMessage(MessageCode.Accounts.ROLES_NOT_FOUND)));
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(roleClient));
            userDetailsCustom.setUsername(clientByUserName.getUsername());
            userDetailsCustom.setPassword(clientByUserName.getPassword());
            userDetailsCustom.setAuthorities(authorities);
            userDetailsCustom.setUserId(clientByUserName.getId());
            return userDetailsCustom;
        }
        else{
            throw new UserDetailNotfoundException(MessageCode.Accounts.ACCOUNT_NOT_FOUND);
        }
    }


}
