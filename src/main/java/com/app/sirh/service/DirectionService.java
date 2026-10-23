
package com.app.sirh.service;

import com.app.sirh.model.ApprobationRecrutement;
import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.Employe;
import com.app.sirh.model.Departement;
import com.app.sirh.model.DemandeStatutEnum;
import com.app.sirh.model.StatutEtapeEnum;
import com.app.sirh.model.EtapeApprobationEnum;

import com.app.sirh.repository.ApprobationRecrutementRepository;
import com.app.sirh.repository.DemandeRecrutementRepository;
import com.app.sirh.repository.EmployeRepository;
import com.app.sirh.repository.PosteRepository;
import com.app.sirh.repository.DepartementRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DirectionService {

    @Autowired
    private ApprobationRecrutementRepository approbationRepository;

    @Autowired
    private DemandeRecrutementRepository demandeRepository;

    @Autowired
    private PosteRepository posteRepository;
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @Autowired
    private DepartementRepository departementRepository;

    @Transactional
public void InsertDemandeBesoin(DemandeRecrutement demande, Long userId,Long departementId) {
        Departement dept = departementRepository.findById(departementId)
            .orElseThrow(() -> new RuntimeException("Département introuvable"));
    Employe employe = employeRepository
            .findByUtilisateurId(userId)
            .orElseThrow(() -> new RuntimeException("Employé introuvable"));

            Long nbrPoste = posteRepository.countByDepartement_Id(departementId);
            if(demande.getNombrePoste() > nbrPoste) {
                throw new RuntimeException("Le nombre de postes demandés dépasse le nombre de postes disponibles dans le département.");
            }
            
    Departement departement = employe.getDepartement();

    if (departement.getCode().equals("DIRECTION")) {

        demande.setDemandeur(employe);
      demande.setDepartement(dept);
        demande.setStatut(DemandeStatutEnum.APPROUVEE);
        System.out.println("Demande approuvée par la direction : " + demande.getId() + " - Statut : " + demande.getStatut()
        +" - Demandeur : " + demande.getDemandeur().getUtilisateur().getNom());
        demandeRepository.save(demande);
    }else {
        throw new RuntimeException("L'employé n'appartient pas au département DIRECTION");
    }
}


public List<DemandeRecrutement> getDemandesByDepartementCode() {
   
        return demandeRepository.findByStatut(DemandeStatutEnum.EN_ATTENTE_DIRECTION);
  
}

public void updateDemandeStatut(Long demandeId, DemandeStatutEnum nouveauStatut) {
    DemandeRecrutement demande = demandeRepository.findById(demandeId)
            .orElseThrow(() -> new RuntimeException("Demande introuvable"));

    demande.setStatut(nouveauStatut);
    demandeRepository.save(demande);
}

public void validerDemande(Long demandeId,Long userId) {
    traiterDemande(demandeId, StatutEtapeEnum.VALIDEE,userId);
}

public void refuserDemande(Long demandeId,Long userId) {
    traiterDemande(demandeId, StatutEtapeEnum.REFUSEE,userId);
}

private void traiterDemande(
        Long demandeId,
        StatutEtapeEnum decision,Long userId) {

    DemandeRecrutement demande = demandeRepository.findById(demandeId)
        .orElseThrow(() ->
            new RuntimeException("Demande introuvable"));

    if (demande.getStatut() != DemandeStatutEnum.EN_ATTENTE_DIRECTION) {
        throw new RuntimeException(
            "La demande n'est pas en attente de décision par la direction"
        );
    }
 Employe employe = employeRepository
            .findByUtilisateurId(userId)
            .orElseThrow(() -> new RuntimeException("Employé introuvable"));
    ApprobationRecrutement approbation = new ApprobationRecrutement();
    approbation.setDemandeRecrutement(demande);
    approbation.setEtape(EtapeApprobationEnum.DIRECTION);
    approbation.setStatutEtape(decision);

    approbation.setValideur(employe);

    approbationRepository.save(approbation);

    if (decision == StatutEtapeEnum.VALIDEE) {
        updateDemandeStatut(
            demandeId,
            DemandeStatutEnum.APPROUVEE
        );
    } else {
        updateDemandeStatut(
            demandeId,
            DemandeStatutEnum.REFUSEE
        );
    }
}
}
