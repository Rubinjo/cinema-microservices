package nl.utwente.movieservice.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public ModelAndView showMovies(@CookieValue(value = "jwt", required = false) String jwt, @RequestParam(value = "q", required = false) String q, Model model) {
		ModelAndView modelAndView = new ModelAndView("movies");
        if (q == null || q.isEmpty()) {
            model.addAttribute("movie", this.movieRepository.findAllByOrderByReleaseAsc());
        } else {
            model.addAttribute("movie", this.movieRepository.findByNameContainingIgnoreCaseOrderByReleaseAsc(q));
        }
        // Add attribute if logged in
        if (jwt != null && checkLogin(jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

	@GetMapping("/new")
    public ModelAndView newMovieForm(@CookieValue(value = "jwt", required = false) String jwt, Model model) {
        ModelAndView modelAndView = new ModelAndView("new_movie_form");
        model.addAttribute("movie", new Movie());
        if (jwt != null && checkLogin(jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

    //Redirect not working
    @PostMapping
    public RedirectView newMovie(@CookieValue(value = "jwt") String jwt, Movie movie, RedirectAttributes attributes) {
        // Check required fields server side
        // Check if authorized
        if (movie.getName() == null || movie.getDescription() == null || movie.getImage() == null || movie.getRelease() == null || checkLogin(jwt) == false) {
            attributes.addAttribute("submitted", false);
            return new RedirectView("movies/new");
        }
        // Save movie
        this.movieRepository.save(movie);

        attributes.addAttribute("submitted", true);
        return new RedirectView("movies/new");
    }

	@GetMapping("/{id}")
    public ModelAndView showMovie(@CookieValue(value = "jwt") String jwt, @PathVariable Long id, Model model) {
        // Check if movie exists
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent()) {
            // Redirect to all movies page
        }
        Movie movie = potMovie.get();
        model.addAttribute("movie", movie);
        // Add attribute if logged in
        if (jwt != null && checkLogin(jwt)) {
            model.addAttribute("login", true);
        }
        ModelAndView modelAndView = new ModelAndView("movie");
        return modelAndView;
    }

    //Redirect not working
	@PutMapping("/{id}")
	public String editMovie(@CookieValue(value = "jwt") String jwt, @PathVariable Long id, Movie movie) {
		// Check if movie exists
        // Check if authorized
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent() || checkLogin(jwt) == false || movie.getId() != potMovie.get().getId()) {
            // Redirect to all movies page
        }
        // Automatically overwrites movie with same id
        this.movieRepository.save(movie);

        return "Not finished";
	}

    //Redirect not working
	@DeleteMapping("/{id}")
	public String deleteMovie(@CookieValue(value = "jwt") String jwt, @PathVariable Long id) {
		// Check if movie exists
        // Check if authorized
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent() || checkLogin(jwt) == false) {
            // Redirect to all movies page
        }
        Movie movie = potMovie.get();

        this.movieRepository.delete(movie);

        return "Not finished";
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
