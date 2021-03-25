package nl.utwente.homeservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class HomeRestController {

    @GetMapping
    public ModelAndView home(@CookieValue(value = "jwt", required = false) String jwt, Model model) {
        // Add attribute if logged in
        if (jwt != null && checkLogin(jwt)) {
            model.addAttribute("login", true);
        }
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login(Model model) {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    // Redirect not working
    @PostMapping
    public ModelAndView authenticate(HttpServletResponse response, String username, String password, Model model) {
        String urlString = "http://localhost:8080/authenticate/account/login";
        HttpURLConnection con = null;
        try {
            // Setup outgoing request
            URL url = new URL(urlString);
            con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Content-Type", "application/json");

            JSONObject auth = new JSONObject();

            auth.put("username", username);
            auth.put("password", password);

            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(auth.toString());
            wr.flush();

            // Get return
            StringBuilder sb = new StringBuilder();  
            BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }
            br.close();
            String jwt = sb.toString();
            // System.out.println(jwt);
            // System.out.println(jwt.substring(8, jwt.length() - 3));
            Cookie cookie = new Cookie("jwt", jwt.substring(8, jwt.length() - 3));
            cookie.setMaxAge(1000 * 60 * 60 * 10);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            response.addCookie(cookie);
              
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        finally {
            if(con != null) {
                con.disconnect();
            }
        }
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

    // Redirect not working
    @PostMapping("/logout")
    public ModelAndView logout(HttpServletResponse response, Model model) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;
    }

    private boolean checkLogin(String jwt) {
        String urlString = "http://localhost:8080/authenticate/check";
        String response = "";
        // Manual HTTP GET request
        HttpURLConnection con = null;
        try {
            // Build HTTP request
            URL url = new URL(urlString);
            con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Authorization", "Bearer " + jwt);

            // Get return
            StringBuilder sb = new StringBuilder();  
            BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }
            br.close();
            response = sb.toString();
            response = response.substring(11, response.length() - 3);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        finally {
            if(con != null) {
                con.disconnect();
            }
        }
        if (response.equals("OK")) {
            return true;
        } else {
            return false;
        }
    }
    
}
