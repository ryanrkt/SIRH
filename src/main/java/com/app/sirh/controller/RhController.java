package com.app.sirh.controller;

import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.DemandeRecrutement.DemandeStatutEnum;
import com.app.sirh.repository.DemandeRecrutementRepository;
import com.app.sirh.repository.EmployeRepository;
import com.app.sirh.security.CustomUserDetails;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rh")
public class RhController {

    private final EmployeRepository employeRepository;
    private final DemandeRecrutementRepository demandeRecrutementRepository;

    public RhController(EmployeRepository employeRepository, 
                        DemandeRecrutementRepository demandeRecrutementRepository) {
        this.employeRepository = employeRepository;
        this.demandeRecrutementRepository = demandeRecrutementRepository;
    }

    @GetMapping("/dashboard")
    public String afficherDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        // Récupération de quelques statistiques utiles pour le dashboard RH
        long totalEmployes = employeRepository.count();
        long totalDemandes = demandeRecrutementRepository.count();

        model.addAttribute("totalEmployes", totalEmployes);
        model.addAttribute("totalDemandes", totalDemandes);
        model.addAttribute("employeConnecte", userDetails.getEmploye());

        return "rh/dashboard";
    }

    @GetMapping("/demandes-recrutement")
    public String listerDemandes(Model model) {
        List<DemandeRecrutement> demandes = demandeRecrutementRepository.findAll();
        model.addAttribute("demandes", demandes);
        return "rh/demande-recrutement-list";
    }

    @PostMapping("/demandes-recrutement/{id}/valider")
    public String validerDemande(@PathVariable Long id) {
        DemandeRecrutement demande = demandeRecrutementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));

        if (demande.getStatut() == DemandeStatutEnum.EN_ATTENTE_RH) {
            demande.setStatut(DemandeStatutEnum.EN_ATTENTE_FINANCE); 
            demandeRecrutementRepository.save(demande);
        }

        return "redirect:/rh/demandes-recrutement?success";
    }

    @PostMapping("/demandes-recrutement/{id}/refuser")
    public String refuserDemande(@PathVariable Long id) {
        DemandeRecrutement demande = demandeRecrutementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable"));

        if (demande.getStatut() == DemandeStatutEnum.EN_ATTENTE_RH) {
            demande.setStatut(DemandeStatutEnum.REFUSEE);
            demandeRecrutementRepository.save(demande);
        }

        return "redirect:/rh/demandes-recrutement?refuse";
    }
}

