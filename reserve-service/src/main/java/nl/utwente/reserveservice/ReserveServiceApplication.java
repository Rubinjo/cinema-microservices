package nl.utwente.reserveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
public class ReserveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReserveServiceApplication.class, args);
	}

}
