package io.skymind.pathmind.api.domain.user.signup.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.skymind.pathmind.api.domain.user.signup.validation.OnCheck;
import io.skymind.pathmind.api.domain.user.signup.validation.OnSignup;
import lombok.Data;

@Data
public class SignUpRequest {

    @JsonProperty("firstName")
    @Size(max = 255, message = "first name length should be less than 250 characters", groups = OnSignup.class)
    @NotEmpty(message = "first name is required", groups = OnSignup.class)
    private String firstName;

    @JsonProperty("lastName")
    @Size(max = 255, message = "last name length should be less than 250 characters", groups = OnSignup.class)
    @NotEmpty(message = "last name is required", groups = OnSignup.class)
    private String lastName;

    @JsonProperty("email")
    @Size(max = 255, message = "email length should be less than 250 characters", groups = OnSignup.class)
    @Email(message = "should be email", groups = {OnSignup.class, OnCheck.class})
    @NotEmpty(message = "email is required", groups = {OnSignup.class, OnCheck.class})
    private String email;

    @JsonProperty("password")
    @NotEmpty(message = "password is required", groups = OnSignup.class)
    @Size(min = 6, max = 50, message = "password length should be between 5 and 50 characters", groups = OnSignup.class)
    @Pattern(regexp = "[\\w\\W]*[a-z]+[\\w\\W]*", message = "password should contain at least one lowercase character", groups = OnSignup.class)
    @Pattern(regexp = "[\\w\\W]*[A-Z]+[\\w\\W]*", message = "password should contain at least one uppercase character", groups = OnSignup.class)
    private String password;

}
