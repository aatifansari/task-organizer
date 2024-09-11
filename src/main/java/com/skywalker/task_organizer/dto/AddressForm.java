package com.skywalker.task_organizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressForm {

    private String addressId;

    @NotBlank(message = "Location is required.")
    @Size( max = 256, message = "Location must be less then 256 characters.")
    private String location;

    @NotBlank(message = "Country is required.")
    private String country;

    @NotBlank(message = "State is required.")
    private String state;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "Pincode is required.")
    private String pincode;
}
