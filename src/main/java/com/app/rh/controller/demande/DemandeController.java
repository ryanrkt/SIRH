package com.app.rh.controller.demande;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemandeController {

    @GetMapping("/demande")
    public String demande() {
        return "demande/demande";
    }
}