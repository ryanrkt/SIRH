package com.app.rh.controller.direction;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DirectionController {

    @GetMapping("/direction")
    public String direction() {
        return "direction/direction";
    }
}