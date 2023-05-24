package com.nanoka.examen;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nanoka.examen.models.Role;
import com.nanoka.examen.models.UserEntity;
import com.nanoka.examen.repositories.IRoleRepository;
import com.nanoka.examen.repositories.IUserRepository;

@SpringBootApplication
public class ExamenApplication implements CommandLineRunner {

	@Autowired
	private IRoleRepository roleRepository;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ExamenApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(!userRepository.existsByUsername("nanoka")){
            UserEntity user = new UserEntity();
			user.setName("Carlos");
			user.setLastname("Escate");
			user.setUsername("nanoka");
			user.setPassword(passwordEncoder.encode("Run4s1m1"));
			user.setEmail("stornblood6969@gmail.com");
			user.setTelephone("941678196");
			user.setProfile("foto.png");
			Role role = roleRepository.findByName("ADMIN").get();
			user.setRoles(Collections.singletonList(role));
			userRepository.save(user);
        }
		
	}
}
