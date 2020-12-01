package io.skymind.pathmind.api.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class SignUpRequest {

    @NotEmpty(message = "first name is required")
    private String firstName;

    @NotEmpty(message = "last name is required")
    private String lastName;

    @Email(message = "should be email")
    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "password is required")
    @Size(min = 5, max = 50, message = "password length should be between 5 and 50 characters")
    @Pattern(regexp = "\\w*[a-z]+\\w*", message = "password should contain at least one lowercase character")
    @Pattern(regexp = "\\w*[A-Z]+\\w*", message = "password should contain at least one uppercase character")
    private String password;

}
