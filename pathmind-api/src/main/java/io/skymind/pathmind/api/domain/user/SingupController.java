package io.skymind.pathmind.api.domain.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import io.skymind.pathmind.api.domain.user.dto.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, ?> processInputError(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        for(FieldError fe : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        return Map.of("timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "message", "Validation errors on form. Total " + exception.getBindingResult().getFieldErrorCount(),
                "path", SIGNUP_URL,
                "errors", fieldErrors);
    }

}
