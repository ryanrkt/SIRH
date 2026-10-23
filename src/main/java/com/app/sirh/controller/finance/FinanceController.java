package com.app.sirh.controller.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceController {

    // @GetMapping("/")
    // public String accueil() {
    //     return "accueil";
    // }

    @GetMapping("/finance/dashboard")
    public String financeDashboard(){
        return "finance/finance";
    }
}