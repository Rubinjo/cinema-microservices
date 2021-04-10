package nl.utwente.foodservice;

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

import nl.utwente.foodservice.controller.FoodRestController;
import nl.utwente.foodservice.repositories.FoodRepository;
import nl.utwente.foodservice.entities.Food;
import org.mockito.Mockito;


@ExtendWith(SpringExtension.class)
@WebMvcTest(FoodRestController.class)
class FoodServiceApplicationTests {
    
    @MockBean
    private FoodRepository foodMockRepository;
    
    @Autowired
    private MockMvc mvc;

    @Test
    void restCheckFood() throws Exception {
        // Send http request to /food
        RequestBuilder request = MockMvcRequestBuilders.get("/food").header("host", "localhost");
        MvcResult result = mvc.perform(request).andReturn();
        // Check if html page gets returned
        assertEquals("text/html;charset=UTF-8", result.getResponse().getContentType());
    }
    
    @Test
    void entityCheckFood() throws Exception {
        Food food = new Food();
        food.setId(1L);
        Mockito.when(this.foodMockRepository.save(food)).thenReturn(food);
        Food newFood = this.foodMockRepository.save(food);
        // Check if saved food corrosponds to created food
        assertEquals(food, newFood);
        // Check if method has been called
        Mockito.verify(this.foodMockRepository).save(food);
    }
}
