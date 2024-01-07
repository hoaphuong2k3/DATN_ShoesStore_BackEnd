package com.example.shoestore.core.product.user.repository;

import com.example.shoestore.core.product.user.dto.response.UserSizeResponse;
import com.example.shoestore.repository.SizeRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSizeRepository extends SizeRepository {

    @Query(nativeQuery = true,value = """
            SELECT distinct sz.id,sz.name
            FROM shoes sh
            inner join shoes_detail shdt on sh.id = shdt.shoes_id
            inner join size sz on shdt.size_id = sz.id where shoes_id = :shoesId
            and shdt.is_deleted = 0 and shdt.status = 1
            """)
    List<UserSizeResponse> getAllByShoesId(@Param("shoesId") Long shoesId);

}
