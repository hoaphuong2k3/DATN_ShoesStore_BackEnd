package com.example.shoestore.core.statistics.repository;

import com.example.shoestore.repository.ClientRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository("statistics")
public interface AdminClientRepository extends ClientRepository {

    @Query(nativeQuery = true, value =
    """
    select count(*) from client c where c.created_time between :fromDate and :toDate
    """)
    Integer getALLClientSighUpCount(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
