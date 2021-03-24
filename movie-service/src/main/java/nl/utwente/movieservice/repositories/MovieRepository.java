package nl.utwente.movieservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import java.util.List;

import nl.utwente.movieservice.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAll();

    List<Movie> findAllByName(String name);

    List<Movie> findByNameContainingIgnoreCase(String name);

}
