package com.example.shoestore.core.account.client.repository;

import com.example.shoestore.core.account.client.model.response.AddressClientResponse;
import com.example.shoestore.repository.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressRepository extends AddressRepository {

    @Query(nativeQuery = true, value = """
                    SELECT `address`.`id`,
                        `address`.`provice_code` as proviceCode,
                        `address`.`district_code` as districtCode,
                        `address`.`commune_code` as communeCode,
                        `address`.`address_detail` as addressDetail,
                        `address`.`created_by` as createdBy,
                        `address`.`updated_by` as updatedBy,
                        `address`.`created_time` as createdTime,
                        `address`.`updated_time` as updatedTime,
                        `address`.`status`,
                        `address`.`is_deleted` as isDeleted,
                        `address`.`client_id` as clientId
                    FROM `database_final_datn_g33321`.`address`
                    WHERE `address`.`client_id` = :idClient
            """)
    Page<AddressClientResponse> pageAddress(@Param("idClient") Long id, Pageable pageable);

}
