package nl.utwente.movieservice.controller;

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

import nl.utwente.movieservice.entities.Movie;
import nl.utwente.movieservice.repositories.MovieRepository;

@RestController
@RequestMapping("/movies")
public class MovieRestController {

    private MovieRepository movieRepository;

    public MovieRestController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public ModelAndView showMovies(@RequestParam(value = "q", required = false) String q, Model model) {
		ModelAndView modelAndView = new ModelAndView("movies");
        if (q == null || q.isEmpty()) {
            model.addAttribute("movie", this.movieRepository.findAll());
        } else {
            model.addAttribute("movie", this.movieRepository.findByNameContainingIgnoreCase(q));
        }
        // model.addAttribute("date", LocalDate.now());
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
    public ModelAndView showMovie(@PathVariable Long id, Model model) {
		ModelAndView modelAndView = new ModelAndView("movie");
        model.addAttribute("movie", this.movieRepository.findById(id));
        return modelAndView;
    }

	@PutMapping("/{id}")
	public String reserveMovie() {
		return "Not finished";
	}
}
