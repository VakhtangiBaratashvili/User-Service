package com.ecommerce.userservice;

import com.ecommerce.userservice.entity.User;
import com.ecommerce.userservice.enums.Role;
import com.ecommerce.userservice.exception.ApiException;
import com.ecommerce.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static java.lang.Boolean.*;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class UserServiceApplication {

	@Value("${application.security.admin.email}")
	private String email;

	@Value("${application.security.admin.phone-number}")
	private String phoneNumber;

	@Value("${application.security.admin.password}")
	private String password;


	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.existsByEmail(email)) {
				log.error("Failed to create admin user, email already exists");
				throw new ApiException("Failed to create admin user, email already exists", HttpStatus.BAD_REQUEST);
			}
			if (userRepository.existsByPhoneNumber(phoneNumber)) {
				log.error("Failed to create admin user, phone number already exists");
				throw new ApiException("Failed to create admin user, phone number already exists", HttpStatus.BAD_REQUEST);
			}

			User admin = User.builder()
					.firstName("Vakhtangi")
					.lastName("Baratashvili")
					.role(Role.ADMIN)
					.email(email)
					.phoneNumber(phoneNumber)
					.password(passwordEncoder.encode(password))
					.enabled(TRUE)
					.accountLocked(FALSE)
					.apiKey(UUID.randomUUID().toString())
					.build();

			userRepository.save(admin);
		};
	}

}
