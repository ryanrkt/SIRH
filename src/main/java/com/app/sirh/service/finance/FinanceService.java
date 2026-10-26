package com.app.sirh.service.finance;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.DemandeStatutEnum;
import com.app.sirh.repository.DemandeRecrutementRepository;

@Service
public class FinanceService {

    private final DemandeRecrutementRepository repository;

    public FinanceService(DemandeRecrutementRepository repository) {
        this.repository = repository;
    }

    public List<DemandeRecrutement> listerDemandes() {
        return repository.findByStatut(DemandeStatutEnum.EN_ATTENTE_FINANCE);
    }

    @Transactional
    public void accepter(Long id) {
        changerStatut(id, DemandeStatutEnum.EN_ATTENTE_DIRECTION);
    }

    @Transactional
    public void refuser(Long id) {
        changerStatut(id, DemandeStatutEnum.REFUSEE);
    }

    private void changerStatut(Long id, DemandeStatutEnum nouveauStatut) {
        DemandeRecrutement d = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Demande introuvable : " + id));

        if (d.getStatut() != DemandeStatutEnum.EN_ATTENTE_FINANCE) {
            throw new IllegalStateException("Cette demande n'est plus en attente de la finance");
        }
        d.setStatut(nouveauStatut);
    }
}