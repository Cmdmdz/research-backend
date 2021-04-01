package com.research.backend.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    private String firstname;
    private String lastname;
    private String faculty;
    private String branch;
    private String address;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> permission;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

//    private String profile;
}
