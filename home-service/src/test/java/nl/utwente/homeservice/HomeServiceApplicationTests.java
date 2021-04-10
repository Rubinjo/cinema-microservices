package nl.utwente.homeservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.utwente.homeservice.controller.HomeRestController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeRestController.class)
class HomeServiceApplicationTests {

	@Autowired
    private MockMvc mvc;

	@Test
    void restCheckHome() throws Exception {
        // Send http request to /movies
        RequestBuilder request = MockMvcRequestBuilders.get("/").header("host", "localhost");
        MvcResult result = mvc.perform(request).andReturn();
        // Check if html page gets returned
        assertEquals("text/html;charset=UTF-8", result.getResponse().getContentType());
    }
}