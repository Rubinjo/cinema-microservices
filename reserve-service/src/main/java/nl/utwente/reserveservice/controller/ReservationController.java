package nl.utwente.reserveservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import nl.utwente.reserveservice.services.MailService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    
	@Autowired
	private MailService mailService;
    
    @PostMapping
    public String sendMail(Integer numTickets, String email) {
        try {
			mailService.sendMail(numTickets, email);
		}catch( Exception e ){
			// catch error
			System.out.println("Error");
		}
        return "Finished";
    }
}
