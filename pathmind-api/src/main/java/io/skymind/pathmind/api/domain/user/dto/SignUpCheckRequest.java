package io.skymind.pathmind.api.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class SignUpCheckRequest {

    @Email(message = "should be email")
    @NotEmpty(message = "email is required")
    private String email;

}
