package com.app.sirh.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.sirh.model.DemandeRecrutement;

@Repository
public interface DemandeRecrutementRepository extends JpaRepository<DemandeRecrutement, Long> {
    
}