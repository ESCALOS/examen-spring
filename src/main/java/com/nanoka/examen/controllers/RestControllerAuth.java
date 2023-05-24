package com.nanoka.examen.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanoka.examen.dtos.DtoAuthResponse;
import com.nanoka.examen.dtos.DtoLogin;
import com.nanoka.examen.dtos.DtoRegister;
import com.nanoka.examen.models.Role;
import com.nanoka.examen.models.UserEntity;
import com.nanoka.examen.repositories.IRoleRepository;
import com.nanoka.examen.repositories.IUserRepository;
import com.nanoka.examen.security.JwtGenerador;

import lombok.AllArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@AllArgsConstructor
@RequestMapping("/api/auth/")
public class RestControllerAuth {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private IRoleRepository roleRepository;
    private IUserRepository userRepository;
    private JwtGenerador jwtGenerador;
    
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody DtoRegister dtoRegister) {
        if(userRepository.existsByUsername(dtoRegister.getUsername())){
            return new ResponseEntity<>("El usuario ya existe", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(dtoRegister.getEmail())) {
            return new ResponseEntity<>("El correo ya está registrado con otro usuario", HttpStatus.BAD_REQUEST);
        }
        UserEntity user = new UserEntity();
        user.setUsername(dtoRegister.getUsername());
        user.setPassword(passwordEncoder.encode(dtoRegister.getPassword()));
		user.setName(dtoRegister.getName());
		user.setLastname(dtoRegister.getLastname());
		user.setEmail(dtoRegister.getEmail());
		user.setTelephone(dtoRegister.getTelephone());
        Role role = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
        return new ResponseEntity<String>("Registro de usuario exitoso", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<DtoAuthResponse> login(@RequestBody DtoLogin dtoLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dtoLogin.getUsername(), dtoLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerador.generateToken(authentication);
        return new ResponseEntity<DtoAuthResponse>(new DtoAuthResponse(token), HttpStatus.OK);
    }

    @GetMapping("usuario-actual")
    public Optional<UserEntity> obtenerUsuarioActual(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

}
