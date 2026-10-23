package com.app.sirh.controller;

import com.app.sirh.dto.DemandeRecrutementForm;
import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.Employe;
import com.app.sirh.model.Poste;
import com.app.sirh.repository.DemandeRecrutementRepository;
import com.app.sirh.repository.PosteRepository;
import com.app.sirh.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/demandes-recrutement")
public class DemandeRecrutementController {

    private final PosteRepository posteRepository;
    private final DemandeRecrutementRepository demandeRecrutementRepository;

    public DemandeRecrutementController(PosteRepository posteRepository,
            DemandeRecrutementRepository demandeRecrutementRepository) {
        this.posteRepository = posteRepository;
        this.demandeRecrutementRepository = demandeRecrutementRepository;
    }

    @GetMapping("/nouvelle")
    public String afficherFormulaire(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Employe employe = userDetails.getEmploye();

        if (employe == null || employe.getDepartement() == null) {
            return "redirect:/login?error=nodept";
        }

        List<Poste> postesDisponibles = posteRepository.findByDepartementId(employe.getDepartement().getId());

        model.addAttribute("departement", employe.getDepartement());
        model.addAttribute("postes", postesDisponibles);
        model.addAttribute("demandeForm", new DemandeRecrutementForm());

        return "demande-recrutement/demande-recrutement-form";
    }

    @PostMapping("/nouvelle")
    public String soumettreDemande(@AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute("demandeForm") DemandeRecrutementForm form,
            BindingResult result, Model model) {
        Employe employe = userDetails.getEmploye();

        if (employe == null || employe.getDepartement() == null) {
            return "redirect:/login";
        }

        Optional<Poste> posteOpt = posteRepository.findByDepartementIdAndTitreIgnoreCase(
                employe.getDepartement().getId(), form.getPosteTitre().trim());

        if (posteOpt.isEmpty()) {
            result.rejectValue("posteTitre", "error.poste",
                    "Le poste sélectionné n'existe pas ou n'appartient pas à votre département.");
        }

        if (result.hasErrors()) {
            model.addAttribute("departement", employe.getDepartement());
            model.addAttribute("postes", posteRepository.findByDepartementId(employe.getDepartement().getId()));
            return "demande-recrutement/demande-recrutement-form";
        }

        DemandeRecrutement demande = new DemandeRecrutement();
        demande.setDepartement(employe.getDepartement());
        demande.setDemandeur(employe);
        demande.setPoste(posteOpt.get());
        demande.setNombrePoste(form.getNombrePoste());
        demande.setSalaire(form.getSalaire()); 

        demandeRecrutementRepository.save(demande);

        return "redirect:/demandes-recrutement/nouvelle?success";
    }
}