package nl.utwente.movieservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.utwente.movieservice.controller.MovieRestController;
import nl.utwente.movieservice.repositories.MovieRepository;
import nl.utwente.movieservice.entities.Movie;
import org.mockito.Mockito;


@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieRestController.class)
class MovieServiceApplicationTests {
    
    @MockBean
    private MovieRepository movieMockRepository;
    
    @Autowired
    private MockMvc mvc;

    @Test
    void restCheckMovies() throws Exception {
        // Send http request to /movies
        RequestBuilder request = MockMvcRequestBuilders.get("/movies").header("host", "localhost");
        MvcResult result = mvc.perform(request).andReturn();
        // Check if html page gets returned
        assertEquals("text/html;charset=UTF-8", result.getResponse().getContentType());
    }
    
    @Test
    void entityCheckMovie() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        Mockito.when(this.movieMockRepository.save(movie)).thenReturn(movie);
        Movie newMovie = this.movieMockRepository.save(movie);
        // Check if saved movie corrosponds to created movie
        assertEquals(movie, newMovie);
        // Check if method has been called
        Mockito.verify(this.movieMockRepository).save(movie);
    }
}
