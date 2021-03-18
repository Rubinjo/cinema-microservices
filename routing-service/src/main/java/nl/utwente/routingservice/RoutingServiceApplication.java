package nl.utwente.routingservice;

import org.springframework.stereotype.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import nl.utwente.routingservice.entities.Movie;
import nl.utwente.routingservice.repositories.MovieRepository;

@Controller
@EnableDiscoveryClient
@SpringBootApplication
public class RoutingServiceApplication {

    MovieRepository movieRepository;

    public RoutingServiceApplication(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

	@GetMapping("/")
    public String showHomepage() {
        return "home";
    }

    @GetMapping("/movies")
    public String showMovies(Model model) {
		// Call search microservice
		model.addAttribute("movie", this.movieRepository.findAll());
        return "movies";
    }

	@GetMapping("/movies/new")
    public String newMovie(Model model) {
        model.addAttribute("movie", new Movie());
        return "new_movie_form";
    }

    @PostMapping("/movies")
    public String newMovie(Movie movie, RedirectAttributes ra) {
        this.movieRepository.save(movie);
        ra.addAttribute("submitted", true);
        return "redirect:/movies/new";
    }

	@GetMapping("/movies/{id}")
    public String showMovie() {
		// Call search microservice
        // model.addAttribute("movie", this.movieRepository.findAllByName());
        return "Not finished";
    }

	@PutMapping("/movies/{id}")
	public String reserveMovie() {
		return "Not finished";
	}
		
	public static void main(String[] args) {
		SpringApplication.run(RoutingServiceApplication.class, args);
	}

}