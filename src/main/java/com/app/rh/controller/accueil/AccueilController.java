<<<<<<<< HEAD:src/main/java/com/app/rh/controller/accueil/AccueilController.java
package com.app.rh.controller.accueil;
========
package com.app.sirh.controller;
>>>>>>>> origin/feature-1-ryan:src/main/java/com/app/sirh/controller/AccueilController.java

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

    @GetMapping("/")
    public String accueil() {
        return "login/login";
    }
}