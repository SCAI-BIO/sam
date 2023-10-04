package de.fraunhofer.scai.bio.sam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
@Controller
public class HomeController {
    
    
    @GetMapping(value = "/")
    public String index() {
        
        return "redirect:swagger-ui.html";
    }
}
