package com.app.sirh.repository;

import com.app.sirh.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    boolean existsByCode(String code);
   
}
