package nl.utwente.authservice;

import java.util.ArrayList;
import net.minidev.json.JSONObject;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.AuthenticationManager;

import nl.utwente.authservice.util.JwtUtil;
import nl.utwente.authservice.controller.AuthRestController;
import nl.utwente.authservice.services.MyUserDetailsService;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthRestController.class)
class AuthServiceApplicationTests {
    
    @MockBean
    private AuthenticationManager authenticationManager;
    
    @MockBean
    private MyUserDetailsService userDetailsService;
    
    @MockBean
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MockMvc mvc;

    @Test
    void loginCheck() throws Exception {
        // Mock operations of beans
        UserDetails userdetails = new User("admin", "admin", new ArrayList<>());
        Mockito.when(this.userDetailsService.loadUserByUsername("admin")).thenReturn(userdetails);
        Mockito.when(this.jwtTokenUtil.generateToken(userdetails)).thenReturn("aeuighaeghiegihegihegnegnjegeg");
        
        JSONObject auth = new JSONObject();
        auth.put("username", "admin");
        auth.put("password", "admin");
        // Send http request to //authenticate/account/login
        RequestBuilder request = MockMvcRequestBuilders.post("/authenticate/account/login").contentType("application/json").content(auth.toString());
        MvcResult result = mvc.perform(request).andReturn();

        // Check if jwt gets returned
        assertEquals("{\"jwt\":\"aeuighaeghiegihegihegnegnjegeg\"}" ,result.getResponse().getContentAsString());
    }

}
