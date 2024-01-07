package com.example.shoestore.core.product.user.repository;

import com.example.shoestore.core.product.user.dto.response.UserColorResponse;
import com.example.shoestore.repository.ColorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserColorRepository extends ColorRepository {
    @Query(nativeQuery = true,value = """
            SELECT distinct cl.id,cl.name
            FROM shoes sh
            inner join shoes_detail shdt on sh.id = shdt.shoes_id
            inner join color cl on shdt.color_id = cl.id where shoes_id =:shoesId
            and shdt.is_deleted = 0 AND shdt.status = 1
            """)
    List<UserColorResponse> getAllByShoesId(@Param("shoesId") Long shoesId);

}
