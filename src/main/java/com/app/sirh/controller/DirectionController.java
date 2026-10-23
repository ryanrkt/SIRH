package com.app.sirh.controller;

import com.app.sirh.model.DemandeRecrutement;
import com.app.sirh.model.Departement;

import com.app.sirh.model.Poste;
import com.app.sirh.security.CustomUserDetails;
import com.app.sirh.service.DirectionService;
import com.app.sirh.repository.PosteRepository;
import com.app.sirh.repository.DepartementRepository ;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DirectionController {
    @Autowired
    DirectionService directionService;


    @Autowired
    PosteRepository posteRepository;
@Autowired
    private DepartementRepository departementRepository;

       @GetMapping("/direction/dashboard")

public String afficherDemandes(Model model) {
    model.addAttribute(
        "demandes",
        directionService.getDemandesByDepartementCode()
    );

    return "direction/direction";
}
@GetMapping("/direction/demandes-recrutement")
public String afficherFormulaire(Model model) {
List<Departement> departements = departementRepository.findAll();
    model.addAttribute("demande", new DemandeRecrutement());

    model.addAttribute("departements", departements);

    return "direction/createDemande";
}

  @PostMapping("/direction/demandes-recrutement")
public String creerDemande(
        @ModelAttribute DemandeRecrutement demande,
        @AuthenticationPrincipal CustomUserDetails userDetails
        , @RequestParam Long departementId
        ) {

    Long userId = userDetails.getUtilisateur().getId();

    directionService.InsertDemandeBesoin(demande, userId, departementId);

    return "redirect:/direction/dashboard";
}
@GetMapping("/direction/postes")
@ResponseBody
public List<Poste> afficherPostes(@RequestParam Long departementId) {
    return posteRepository.findByDepartementId(departementId);
}


@PostMapping("/direction/demandes-recrutement/valider")
public String validerDemande(
        @RequestParam("id") Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getUtilisateur().getId();

    directionService.validerDemande(id, userId);

    return "redirect:/direction/dashboard";
}

@PostMapping("/direction/demandes-recrutement/refuser")
public String refuserDemande(
        @RequestParam("id") Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

    Long userId = userDetails.getUtilisateur().getId();

    directionService.refuserDemande(id, userId);

    return "redirect:/direction/dashboard";
}
}