package nl.utwente.reserveservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private JavaMailSender javaMailSender;

    @Autowired
	public MailService(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}

    @Async
    public void sendMail(Integer numTickets, String email) throws MailException, InterruptedException {

        System.out.println("Sending email...");

        SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(email);
		mail.setFrom("Cinema@gmail.com");
		mail.setSubject("Reservation for Movie");
		mail.setText("Thank you for your reservation by Cinema Utwente. The popcorn has already been popped and our staff is ready at your service.\n\nYou have reserved " + numTickets + " ticket(s).\n\n*This is an email from a sample cinema application, this mail is not intended for real use.\n\nWith kind regards,\nThe team of Cinema Utwente");
		javaMailSender.send(mail);

		System.out.println("Email Sent!");
	}

}
