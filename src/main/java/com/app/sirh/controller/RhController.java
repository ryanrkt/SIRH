package com.app.sirh.controller;

import com.app.sirh.repository.DemandeRecrutementRepository;
import com.app.sirh.repository.EmployeRepository;
import com.app.sirh.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}