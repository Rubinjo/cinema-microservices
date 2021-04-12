package nl.utwente.reserveservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import nl.utwente.reserveservice.services.MailService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private MailService mailService;
    
    @PostMapping
    public ModelAndView sendMail(Integer numTickets, String email, ModelMap model) {
        try {
			mailService.sendMail(numTickets, email);
		}catch( Exception e ){
			System.out.println("Error");
		}
        return new ModelAndView("redirect:/movies", model);
    }
}
