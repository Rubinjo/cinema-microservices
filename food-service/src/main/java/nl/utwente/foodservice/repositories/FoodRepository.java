package nl.utwente.foodservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import java.util.List;

import nl.utwente.foodservice.entities.Food;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findAll();

    List<Food> findAllByName(String name);

    List<Food> findByNameContainingIgnoreCase(String name);

}
