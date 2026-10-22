
package com.app.sirh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.sirh.model.Utilisateur;
import com.app.sirh.service.AuthService;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute Utilisateur utilisateur,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            String message = authService.registerCandidat(utilisateur);

            if (message.startsWith("Erreur :")) {
                model.addAttribute("error", message);
                return "auth/register";
            }

            redirectAttributes.addFlashAttribute(
                "success",
                message + " Vous pouvez maintenant vous connecter."
            );

            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute(
                "error",
                "Une erreur technique est survenue lors de l'inscription. Veuillez réessayer."
            );

            return "auth/register";
        }
    }
}
