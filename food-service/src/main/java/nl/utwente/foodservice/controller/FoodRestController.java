package nl.utwente.foodservice.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.ModelMap;

import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestHeader;

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
    public ModelAndView showFood(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, Model model) {
		ModelAndView modelAndView = new ModelAndView("food");
        model.addAttribute("food", this.foodRepository.findAll());
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newFood(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, Model model) {
        ModelAndView modelAndView = new ModelAndView("new_food_form");
        model.addAttribute("food", new Food());
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

    @PostMapping
    public ModelAndView newFood(@RequestHeader String host, @CookieValue(value = "jwt") String jwt, Food food, ModelMap model) {
        // Check required fields server side
        // Check if authorized
        if (food.getName() == null || food.getDescription() == null || food.getImage() == null || food.getPrice() == null || food.getType() == null || checkLogin(host, jwt) == false) {
            // Needs to be added
        }
        // Save food
        this.foodRepository.save(food);
        
        return new ModelAndView("redirect:/food/new", model);
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editFood(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, @PathVariable Long id, Model model, ModelMap remodel) {
        Optional <Food> potFood = this.foodRepository.findById(id);
        if (!potFood.isPresent()) {
            // Redirect to all food page
            remodel.addAttribute("e", "notfound");
            return new ModelAndView("redirect:/food", remodel);
        }
        Food food = potFood.get();
        model.addAttribute("food", food);
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        ModelAndView modelAndView = new ModelAndView("edit_food_form");
        return modelAndView;
    }

    //Redirect managed through JQuery AJAX
	@PutMapping("/{id}")
	public ResponseEntity editMovie(@RequestHeader String host, @CookieValue(value = "jwt") String jwt, @PathVariable Long id, Food food) {
		// Check if movie exists
        // Check if authorized
        // Check required fields server side
        Optional <Food> potFood = this.foodRepository.findById(id);
        if (!potFood.isPresent() || checkLogin(host, jwt) == false || food.getId() != potFood.get().getId() || food.getName() == null || food.getDescription() == null || food.getImage() == null || food.getPrice() == null || food.getType() == null) {
            // Redirect to all movies page
            return ResponseEntity.badRequest().build();
        }
        // Automatically overwrites movie with same id
        this.foodRepository.save(food);

        return ResponseEntity.ok().build();
	}

    //Redirect managed through JQuery AJAX
	@DeleteMapping("/{id}")
	public ResponseEntity deleteFood(@RequestHeader String host, @CookieValue(value = "jwt") String jwt, @PathVariable Long id) {
		// Check if movie exists
        // Check if authorized
        Optional <Food> potFood = this.foodRepository.findById(id);
        if (!potFood.isPresent() || checkLogin(host, jwt) == false) {
            // Redirect to all movies page
            return ResponseEntity.badRequest().build();
        }
        Food food = potFood.get();

        this.foodRepository.delete(food);

        return ResponseEntity.ok().build();
	}

    private boolean checkLogin(String host, String jwt) {
        String urlString = "http://cinetopia.ut/authenticate/check";
        if (host.contains("host.docker.internal")) {
            urlString = "http://localhost:8080/authenticate/check";
        }
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
