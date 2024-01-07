package com.example.shoestore.entity;

import com.example.shoestore.entity.base.PrimaryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member_account_role")
public class AccountRole extends PrimaryEntity {

    @Column(name = "id_member_account")
    private Long idAccount;

    @Column(name = "id_role")
    private Long idRole;


}
