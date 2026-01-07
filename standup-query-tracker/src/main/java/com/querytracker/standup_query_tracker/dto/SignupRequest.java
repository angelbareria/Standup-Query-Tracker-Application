package com.querytracker.standup_query_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author abareria
 **/
@Data
public class SignupRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Password Required")
    private String password;

    @NotBlank(message = "Role is Required")
    private String role;

}
