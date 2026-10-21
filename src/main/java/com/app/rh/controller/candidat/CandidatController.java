package com.app.rh.controller.candidat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CandidatController {
    @GetMapping("/candidats")
    public String candidats() {
        return "candidat/candidats";
    }

    @GetMapping("/candidat/details")
    public String candidatDetails() {
        return "candidat/candidat-detail";
    }

    @GetMapping("/candidature")
    public String candidature() {
        return "candidat/candidature";
    }
}