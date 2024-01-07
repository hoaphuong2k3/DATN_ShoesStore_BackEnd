package com.example.shoestore.sercurity.provider.repository;

import com.example.shoestore.entity.Client;
import com.example.shoestore.repository.ClientRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityClientRepository extends ClientRepository {

    @Query(nativeQuery = true, value = "SELECT * FROM client cl WHERE cl.userName = :username AND cl.status = 1")
    Client getClientByUsername(@Param("username") String username);

    @Query(nativeQuery = true, value = "SELECT * FROM client cl WHERE cl.email = :email AND cl.status = 1")
    Client getClientByEmail(@Param("email") String email);
}
