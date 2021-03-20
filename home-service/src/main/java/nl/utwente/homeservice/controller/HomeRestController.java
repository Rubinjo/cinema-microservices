package nl.utwente.homeservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeRestController {

    @GetMapping("/")
    public ModelAndView newMovie(Model model) {
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }
    
}
