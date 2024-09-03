package com.skywalker.task_organizer.dto;

import lombok.Data;

@Data
public class RegisterForm {

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private Integer isdCode;

    private Long phoneNumber;

    private String faxNumber;

    private String password;

    private AddressForm addressForm;
}
