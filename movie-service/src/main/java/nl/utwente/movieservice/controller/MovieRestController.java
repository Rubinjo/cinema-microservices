package nl.utwente.movieservice.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.RequestMapping;

import nl.utwente.movieservice.entities.Movie;
import nl.utwente.movieservice.repositories.MovieRepository;

@RestController
@RequestMapping("/movies")
public class MovieRestController {

    private MovieRepository movieRepository;

    public MovieRestController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // @GetMapping("/")
    // public ModelAndView showHomepage() {
    //     ModelAndView modelAndView = new ModelAndView("home");
    //     return modelAndView;
    // }

    @GetMapping
    public ModelAndView showMovies(Model model) {
		// Call search microservice
        ModelAndView modelAndView = new ModelAndView("movies");
		model.addAttribute("movie", this.movieRepository.findAll());
        return modelAndView;
    }

	@GetMapping("/new")
    public ModelAndView newMovie(Model model) {
        ModelAndView modelAndView = new ModelAndView("new_movie_form");
        model.addAttribute("movie", new Movie());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView newMovie(@CookieValue(value = "jwt") String jwt, Movie movie, Model model) {
        String urlString = "http://localhost:8080/authenticate/check";
        
        // Manual HTTP GET request
        StringBuffer content = new StringBuffer();
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
            String response = sb.toString();
            response = response.substring(11, response.length() - 3);
            if (response.equals("OK")) {
                this.movieRepository.save(movie);
                // ra.addAttribute("submitted", true);
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        finally {
            if(con != null) {
                con.disconnect();
            }
        }
        ModelAndView modelAndView = new ModelAndView("new_movie_form");
        return modelAndView;
    }

	@GetMapping("/{id}")
    public String showMovie() {
		// Call search microservice
        // model.addAttribute("movie", this.movieRepository.findAllByName());
        return "Not finished";
    }

	@PutMapping("/{id}")
	public String reserveMovie() {
		return "Not finished";
	}
}
