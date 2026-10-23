package com.app.sirh.repository;

import com.app.sirh.model.ApprobationRecrutement;
import com.app.sirh.model.EtapeApprobationEnum;
import com.app.sirh.model.StatutEtapeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprobationRecrutementRepository extends JpaRepository<ApprobationRecrutement, Long> {
    List<ApprobationRecrutement> findByDemandeRecrutementId(Long demandeId);

    Optional<ApprobationRecrutement> findByDemandeRecrutementIdAndEtape(
            Long demandeId, EtapeApprobationEnum etape);

    List<ApprobationRecrutement> findByValideurId(Long valideurId);

    List<ApprobationRecrutement> findByStatutEtape(StatutEtapeEnum statut);
}
