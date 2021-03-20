package nl.utwente.userservice.initialize;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import nl.utwente.userservice.data.UserRepository;
import nl.utwente.userservice.domain.User;

@Component
public class UserDataInitializer implements SmartInitializingSingleton {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void afterSingletonsInstantiated() {
		User user = new User(1, "Ruben Lucas", "r.h.lucas@student.utwente.nl", passwordEncoder
			.encode("admin"),
				Collections
					.singletonList("ROLE_ADMIN"),
				Instant
					.now(),
				true);
		User user2 = new User(2, "Bas van Tintelen", "b.f.m.vantintelen@student.utwente.nl", passwordEncoder
			.encode("admin"),
				Collections
					.singletonList("ROLE_ADMIN"),
				Instant
					.now(),
				false);

		userRepository.saveAll(Arrays.asList(user, user2)).subscribe();
	}

}
