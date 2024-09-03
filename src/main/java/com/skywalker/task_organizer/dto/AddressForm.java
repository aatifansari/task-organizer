package com.skywalker.task_organizer.dto;

import lombok.Data;

@Data
public class AddressForm {

    private String addressId;

    private String location;

    private String country;

    private String state;

    private String city;

    private Integer pincode;
}
