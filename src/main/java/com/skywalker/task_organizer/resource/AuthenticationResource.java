package com.skywalker.task_organizer.resource;

import com.skywalker.task_organizer.dto.LoginForm;
import com.skywalker.task_organizer.dto.LoginResponse;
import com.skywalker.task_organizer.dto.RegisterForm;
import com.skywalker.task_organizer.entity.User;
import com.skywalker.task_organizer.service.AuthenticationService;
import com.skywalker.task_organizer.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);

    public AuthenticationResource(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    private ResponseEntity<User> register(@RequestBody RegisterForm registerForm){
        User registeredUser = null;
        try{
            registeredUser = authenticationService.signup(registerForm);
            return new ResponseEntity<>(registeredUser, OK);
        }catch(DataIntegrityViolationException e){
            LOGGER.error("Email already exists ", e);
            return new ResponseEntity<>(BAD_REQUEST);
        }catch(Exception e){
            LOGGER.error("Unexpected Error ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginForm loginForm){
        User authenticatedUser = authenticationService.authenticate(loginForm);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}
