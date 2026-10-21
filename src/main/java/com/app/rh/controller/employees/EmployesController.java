package com.app.rh.controller.employees;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployesController {

    @GetMapping("/employes")
    public String employes() {
        return "employes/employes";
    }
}