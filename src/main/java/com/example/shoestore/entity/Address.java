package com.example.shoestore.entity;

import com.example.shoestore.entity.base.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
@Builder
public class Address extends AuditEntity {

    @Column(name = "provice_code")
    private String proviceCode;

    @Column(name = "district_code")
    private String districtCode;

    @Column(name = "commune_code")
    private String communeCode;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "status")
    private Integer status;

    @Column(name = "client_id")
    private Long idClient;

}
