// package nl.utwente.gateway;

// import java.util.function.Function;

// import org.springframework.cloud.gateway.route.Route.AsyncBuilder;
// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.PredicateSpec;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
// import java.util.function.Predicate.*;

// @Configuration
// @EnableDiscoveryClient
// public class RouteConfig {
// 	@Bean
// 	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//         return builder.routes()
// 			.route("users", r -> r.path("/users/**")
// 				.uri("lb://USER")
// 			)
// 			.route("movies", r -> r.path("/movies/**")
// 				.uri("lb://MOVIE")
// 			)
// 			.build();
//     }
// }
