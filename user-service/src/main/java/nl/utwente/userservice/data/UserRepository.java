package nl.utwente.userservice.data;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import nl.utwente.userservice.domain.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long>, ReactiveQueryByExampleExecutor<User> {
	Mono<User> findByEmail(String email);

	Flux<User> findByEmailContainingAndRolesContainingAllIgnoreCaseAndEnabledIsTrue(String email, String role);
}
