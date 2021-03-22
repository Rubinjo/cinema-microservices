package nl.utwente.movieservice.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.ui.Model;
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
    public String newMovie(Movie movie, RedirectAttributes ra) {
        String urlString = "http:localhost:8080/authenticate/account/login";
        
        // Manual HTTP GET request
        StringBuffer content = new StringBuffer();
        HttpURLConnection con = null;
        try {
            // Build HTTP request
            URL url = new URL(urlString);
            con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authenticate", "...............");

            // Transform the response InputStream into a String
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        finally {
            if(con != null) {
                con.disconnect();
            }
        }

        System.out.print(content);

        // Transform the String to JSON with Jackson
        // WeatherResult weatherResult;
        // try {
        //    weatherResult = jacksonObjectMapper.readValue(content.toString(), WeatherResult.class);
        // }
        // catch(Exception e){
        //     throw new RuntimeException(e);
        // }


        this.movieRepository.save(movie);
        ra.addAttribute("submitted", true);
        return "redirect:/movies/new";
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
