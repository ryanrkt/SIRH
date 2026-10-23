package com.app.sirh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.sirh.model.Employe;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    
    Optional<Employe> findByUtilisateurId(Long utilisateurId);

    
}