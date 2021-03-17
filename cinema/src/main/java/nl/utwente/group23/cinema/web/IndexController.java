package nl.utwente.group23.cinema.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String showWeather(Model model) {
        return "home";
    }

    
}
