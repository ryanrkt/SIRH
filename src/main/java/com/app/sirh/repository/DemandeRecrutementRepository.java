package com.app.sirh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.DemandeStatutEnum;

@Repository
public interface DemandeRecrutementRepository extends JpaRepository<DemandeRecrutement, Long> {
    List<DemandeRecrutement> findByDepartementId(Long departementId);

    List<DemandeRecrutement> findByDemandeurId(Long demandeurId);

    List<DemandeRecrutement> findByStatut(DemandeStatutEnum statut);

    @Query("""
        select d from DemandeRecrutement d
        join fetch d.departement
        join fetch d.demandeur
        join fetch d.poste
        order by d.createdAt desc
    """)
    List<DemandeRecrutement> findAllForFinance();

   
}
