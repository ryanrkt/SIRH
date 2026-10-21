package com.app.rh.controller.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceController {

    @GetMapping("/finance")
    public String finance() {
        return "finance/finance";
    }
}