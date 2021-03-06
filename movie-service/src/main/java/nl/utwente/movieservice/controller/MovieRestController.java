package nl.utwente.movieservice.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ModelAndView showMovies(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, @RequestParam(value = "q", required = false) String q, @RequestParam(value = "e", required = false) String e, Model model) {
		ModelAndView modelAndView = new ModelAndView("movies");
        if (q == null || q.isEmpty()) {
            model.addAttribute("movie", this.movieRepository.findAllByOrderByReleaseAsc());
        } else {
            model.addAttribute("movie", this.movieRepository.findByNameContainingIgnoreCaseOrderByReleaseAsc(q));
        }
        // Add error attribute if exists
        if (e != null) {
            model.addAttribute("e", e);
        }
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newMovieForm(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, @RequestParam(value = "submitted", required = false) String submitted, Model model) {
        ModelAndView modelAndView = new ModelAndView("new_movie_form");
        model.addAttribute("movie", new Movie());
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        if (submitted != null) {
            model.addAttribute("submitted", submitted);
        }
        return modelAndView;
    }

    @PostMapping
    public ModelAndView newMovie(@RequestHeader String host, @CookieValue(value = "jwt") String jwt, Movie movie, ModelMap model) {
        // Check required fields server side
        // Check if authorized
        if (movie.getName() == null || movie.getDescription() == null || movie.getImage() == null || movie.getRelease() == null || checkLogin(host, jwt) == false) {
            model.addAttribute("submitted", false);
            return new ModelAndView("redirect:/movies/new", model);
        }
        // Save movie
        this.movieRepository.save(movie);
        model.addAttribute("submitted", true);
        return new ModelAndView("redirect:/movies/new", model);
    }

    @GetMapping("/{id}")
    public ModelAndView showMovie(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, @PathVariable Long id, Model model, ModelMap remodel) {
        // Check if movie exists
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent()) {
            // Redirect to all movies page
            remodel.addAttribute("e", "notfound");
            return new ModelAndView("redirect:/movies", remodel);
        }
        Movie movie = potMovie.get();
        model.addAttribute("movie", movie);
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        ModelAndView modelAndView = new ModelAndView("movie");
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editMovie(@RequestHeader String host, @CookieValue(value = "jwt", required = false) String jwt, @PathVariable Long id, Model model, ModelMap remodel) {
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent()) {
            // Redirect to all movies page
            remodel.addAttribute("e", "notfound");
            return new ModelAndView("redirect:/movies", remodel);
        }
        Movie movie = potMovie.get();
        model.addAttribute("movie", movie);
        // Add attribute if logged in
        if (jwt != null && checkLogin(host, jwt)) {
            model.addAttribute("login", true);
        }
        ModelAndView modelAndView = new ModelAndView("edit_movie_form");
        return modelAndView;
    }

    //Redirect managed through JQuery AJAX
	@PutMapping("/{id}")
	public ResponseEntity editMovie(@RequestHeader String host, @CookieValue(value = "jwt") String jwt, @PathVariable Long id, Movie movie) {
		// Check if movie exists
        // Check if authorized
        // Check required fields server side
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent() || checkLogin(host, jwt) == false || movie.getId() != potMovie.get().getId() || movie.getName() == null || movie.getDescription() == null || movie.getImage() == null || movie.getRelease() == null) {
            // Redirect to all movies page
            return ResponseEntity.badRequest().build();
        }
        // Automatically overwrites movie with same id
        this.movieRepository.save(movie);

        return ResponseEntity.ok().build();
	}

    //Redirect managed through JQuery AJAX
	@DeleteMapping("/{id}")
	public ResponseEntity deleteMovie(@RequestHeader String host, @CookieValue(value = "jwt") String jwt, @PathVariable Long id) {
		// Check if movie exists
        // Check if authorized
        Optional <Movie> potMovie = this.movieRepository.findById(id);
        if (!potMovie.isPresent() || checkLogin(host, jwt) == false) {
            // Redirect to all movies page
            return ResponseEntity.badRequest().build();
        }
        Movie movie = potMovie.get();

        this.movieRepository.delete(movie);

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
