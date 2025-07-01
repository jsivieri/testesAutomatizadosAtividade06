package com.iftm.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.iftm.client.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findByIncome(Double income, Pageable pageable);
}
