package com.app.sirh.repository;

import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.DemandeStatutEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRecrutementRepository extends JpaRepository<DemandeRecrutement, Long> {
    List<DemandeRecrutement> findByDepartementId(Long departementId);

    List<DemandeRecrutement> findByDemandeurId(Long demandeurId);

    List<DemandeRecrutement> findByStatut(DemandeStatutEnum statut);
    
   
}
