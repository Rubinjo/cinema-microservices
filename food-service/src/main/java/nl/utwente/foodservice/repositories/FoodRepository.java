package nl.utwente.foodservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

import nl.utwente.foodservice.entities.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findAll();

    Optional<Food> findById(Long id);

}
