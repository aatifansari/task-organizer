package com.skywalker.task_organizer.service;

import com.skywalker.task_organizer.dto.LoginForm;
import com.skywalker.task_organizer.dto.RegisterForm;
import com.skywalker.task_organizer.entity.Address;
import com.skywalker.task_organizer.entity.User;
import com.skywalker.task_organizer.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(RegisterForm registerForm) {
        LOGGER.info("Register Form : {}", registerForm);
        var addressForm = registerForm.getAddressForm();
        String userId = UUID.randomUUID().toString();
        var address = new Address().builder()
                .addressId(UUID.randomUUID().toString())
                .location(addressForm.getLocation())
                .country(addressForm.getCountry())
                .state(addressForm.getState())
                .city(addressForm.getCity())
                .pincode(addressForm.getPincode())
                .isDeleted(false)
                .createdAt(Instant.now())
                .createdBy(userId)
                .updatedAt(Instant.now())
                .createdBy(userId)
                .build();


        var user = new User().builder()
                .userId(userId)
                .firstName(registerForm.getFirstName())
                .lastName(registerForm.getLastName())
                .email(registerForm.getEmail())
                .isdCode(registerForm.getIsdCode())
                .phoneNumber(registerForm.getPhoneNumber())
                .faxNumber(registerForm.getFaxNumber())
                .password(passwordEncoder.encode(registerForm.getPassword()))
                .address(address)
                .isDeleted(false)
                .createdAt(Instant.now())
                .createdBy(userId)
                .updatedAt(Instant.now())
                .updatedBy(userId)
                .build();

        return userRepository.save(user);
    }

    public User authenticate(LoginForm loginForm){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword())
        );
        return userRepository.findByEmail(loginForm.getEmail()).orElseThrow();
    }


}