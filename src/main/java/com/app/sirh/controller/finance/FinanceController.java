package com.app.sirh.controller.finance;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.sirh.service.finance.FinanceService;

// @Controller
// public class FinanceController {
//     // @GetMapping("/")
//     // public String accueil() {
//     //     return "accueil";
//     // }
//     @GetMapping("/finance/dashboard")
//     public String financeDashboard(){
//         return "finance/finance";
//     }
// }
@Controller
@RequestMapping("/finance/dashboard")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("demandes", financeService.listerDemandes());
        return "finance/finance";
    }

    @PostMapping("/{id}/accepter")
    @ResponseBody
    public ResponseEntity<Map<String, String>> accepter(@PathVariable Long id) {
        return traiter(() -> financeService.accepter(id), "Demande envoyée à la direction.");
    }

    @PostMapping("/{id}/refuser")
    @ResponseBody
    public ResponseEntity<Map<String, String>> refuser(@PathVariable Long id) {
        return traiter(() -> financeService.refuser(id), "Demande refusée.");
    }

    private ResponseEntity<Map<String, String>> traiter(Runnable action, String messageSucces) {
        try {
            action.run();
            return ResponseEntity.ok(Map.of("message", messageSucces));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }
}
