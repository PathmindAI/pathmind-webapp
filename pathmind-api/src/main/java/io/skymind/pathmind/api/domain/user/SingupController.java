package io.skymind.pathmind.api.domain.user;

import javax.validation.Valid;

import io.skymind.pathmind.api.domain.user.dto.SignUpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.skymind.pathmind.api.domain.user.SingupController.SIGNUP_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = SIGNUP_URL)
public class SingupController {

    public static final String SIGNUP_URL = "/signup";

    @PostMapping(path = {"", "/"}, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public String signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return null;
    }

}
