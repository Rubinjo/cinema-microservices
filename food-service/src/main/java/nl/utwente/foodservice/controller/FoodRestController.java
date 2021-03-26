package nl.utwente.foodservice.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nl.utwente.foodservice.entities.Food;
import nl.utwente.foodservice.repositories.FoodRepository;

@RestController
@RequestMapping("/food")
public class FoodRestController {

    private FoodRepository foodRepository;

    public FoodRestController(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @GetMapping
    public ModelAndView showFood(@CookieValue(value = "jwt", required = false) String jwt, Model model) {
		ModelAndView modelAndView = new ModelAndView("food");
        model.addAttribute("food", this.foodRepository.findAll());
        // Add attribute if logged in
        if (jwt != null && checkLogin(jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

	@GetMapping("/new")
    public ModelAndView newFood(@CookieValue(value = "jwt", required = false) String jwt, Model model) {
        ModelAndView modelAndView = new ModelAndView("new_food_form");
        model.addAttribute("food", new Food());
        // Add attribute if logged in
        if (jwt != null && checkLogin(jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

    @PostMapping
    public ModelAndView newFood(@CookieValue(value = "jwt") String jwt, Food food, Model model) {
        // Check required fields server side
        // Check if authorized
        if (food.getName() == null || food.getDescription() == null || food.getImage() == null || food.getPrice() == null || food.getType() == null || checkLogin(jwt) == false) {
            // Needs to be added
        }
        // Save food
        this.foodRepository.save(food);
        
        ModelAndView modelAndView = new ModelAndView("new_food_form");
        return modelAndView;
    }

    private boolean checkLogin(String jwt) {
        String urlString = "http://localhost:8080/authenticate/check";
        String response = "";
        // Manual HTTP GET request
        HttpURLConnection con = null;
        try {
            // Build HTTP request
            URL url = new URL(urlString);
            con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Authorization", "Bearer " + jwt);

            // Get return
            StringBuilder sb = new StringBuilder();  
            BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }
            br.close();
            response = sb.toString();
            response = response.substring(11, response.length() - 3);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        finally {
            if(con != null) {
                con.disconnect();
            }
        }
        if (response.equals("OK")) {
            return true;
        } else {
            return false;
        }
    }
}
