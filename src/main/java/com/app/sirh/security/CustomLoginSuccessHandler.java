package com.app.sirh.security;

import com.app.sirh.model.Employe;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String roleCode = userDetails.getUtilisateur().getRole().getCode();
        
        String redirectUrl = "/offres-emploi"; // Redirection par défaut (Candidats / Employés simples)

        if ("MANAGER".equals(roleCode)) {
            Employe employe = userDetails.getEmploye();
            
            if (employe != null && employe.getDepartement() != null) {
                String deptCode = employe.getDepartement().getCode();
                
                // Routage selon le code du département du Manager
                switch (deptCode) {
                    case "RH":
                        redirectUrl = "/rh/dashboard";
                        break;
                    case "FINANCE":
                        redirectUrl = "/finance/dashboard";
                        break;
                    case "DIRECTION":
                        redirectUrl = "/direction/dashboard";
                        break;
                    default:
                        // Autres départements (ex: IT, MARKETING) -> interface de demande de recrutement
                        redirectUrl = "/dept-"+deptCode+"/demandes-recrutement";
                        break;
                }
            } else {
                // Sécurité de secours si le manager n'a pas de département rattaché
                redirectUrl = "/dashboard/demandes-recrutement";
            }
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
