package com.app.rh.controller.offre;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OffreController {

    @GetMapping("/offres")
    public String offres() {
        return "offre/offres";
    }

    @GetMapping("/offre/creation")
    public String offreCreation() {
        return "offre/offre-creation";
    }

    @GetMapping("/offre/details")
    public String offreDetail() {
        return "offre/offre-detail";
    }

}
