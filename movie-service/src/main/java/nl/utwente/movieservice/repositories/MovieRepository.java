package nl.utwente.movieservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

import nl.utwente.movieservice.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOrderByReleaseAsc();

    Optional<Movie> findById(Long id);

    List<Movie> findByNameContainingIgnoreCaseOrderByReleaseAsc(String name);

}
