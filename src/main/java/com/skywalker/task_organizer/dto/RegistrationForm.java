package com.skywalker.task_organizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationForm {

    private String userId;

    @NotBlank(message = "First Name is required")
    @Size(min = 3, max = 20, message = "First Name must be from 3 to 20 characters.")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 3, max = 20, message = "Last Name must be from 3 to 20 characters.")
    private String lastName;

    @NotEmpty(message = "The email is required.")
    @Email(message = "The email is not a valid email.")
    private String email;

    @NotBlank(message = "ISD code is required")
    private String isdCode;

    @NotBlank(message = "The phone number is required.")
    @Size(min = 10, max = 10, message = "Phone number must be of 10 digits")
    private String phoneNumber;

    private String faxNumber;

    @NotBlank(message = "The password is required.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$", message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
    private String password;

    @Valid
    @NotNull(message = "The address is required.")
    private AddressForm addressForm;
}
