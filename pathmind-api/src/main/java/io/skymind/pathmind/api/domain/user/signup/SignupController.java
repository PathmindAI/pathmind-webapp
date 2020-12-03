package io.skymind.pathmind.api.domain.user.signup;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import io.skymind.pathmind.api.domain.user.signup.dto.SignUpRequest;
import io.skymind.pathmind.api.domain.user.signup.validation.OnCheck;
import io.skymind.pathmind.api.domain.user.signup.validation.OnSignup;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.services.analytics.SegmentTrackerService;
import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.shared.data.PathmindUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import static io.skymind.pathmind.api.domain.user.signup.SignupController.SIGNUP_URL;
import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.EVENT_SIGN_UP;
import static io.skymind.pathmind.shared.segment.SegmentTrackingEvents.EVENT_VERIFICATION_EMAIL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(path = SIGNUP_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Validated
public class SignupController {

    public static final String SIGNUP_URL = "/signup";

    private final URI webappSignupUri;
    private final UserDAO userDAO;
    private final SegmentTrackerService segmentTrackerService;
    private final EmailNotificationService emailNotificationService;

    public SignupController(@Value("${pm.api.webapp.url}") String webappDomainUrl,
                            UserDAO userDAO,
                            SegmentTrackerService segmentTrackerService,
                            EmailNotificationService emailNotificationService) {
        this.webappSignupUri = UriComponentsBuilder.fromHttpUrl(webappDomainUrl).path("sign-in").build().toUri();
        this.userDAO = userDAO;
        this.segmentTrackerService = segmentTrackerService;
        this.emailNotificationService = emailNotificationService;
    }

    @PostMapping
    @Validated(OnSignup.class)
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        ResponseEntity<?> res = check(signUpRequest);
        if (!res.getStatusCode().is2xxSuccessful()) {
            return res;
        }

        PathmindUser user = new PathmindUser();
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstname(signUpRequest.getFirstName());
        user.setLastname(signUpRequest.getLastName());
        user.setEmailVerificationToken(UUID.randomUUID());
        long id = userDAO.insertUser(user);
        log.info("New user signed up: " + user.getEmail());
        final PathmindUser newUser = userDAO.findById(id);
        segmentTrackerService.track(EVENT_SIGN_UP, String.valueOf(id), new HashMap<>() {{
                    put("userId", String.valueOf(newUser.getId()));
                    put("userName", newUser.getName());
                    put("userEmail", newUser.getEmail());
                }}
        );
        emailNotificationService.sendVerificationEmail(user, user.getEmail(), true);
        segmentTrackerService.track(EVENT_VERIFICATION_EMAIL, String.valueOf(id), new HashMap<>() {{
                    put("userId", String.valueOf(newUser.getId()));
                    put("userName", newUser.getName());
                    put("userEmail", newUser.getEmail());
                }}
        );
        return ResponseEntity.created(webappSignupUri).build();
    }

    @PostMapping(path = "/check")
    @Validated(OnCheck.class)
    public ResponseEntity<Boolean> check(@RequestBody @Valid SignUpRequest checkRequest) {
        final String email = StringUtils.trimToEmpty(checkRequest.getEmail());
        if (userDAO.findByEmailIgnoreCase(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public Map<String, Object> processInputError(ConstraintViolationException exception, HttpServletRequest req) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation v : exception.getConstraintViolations()) {
            String[] dottedPath = v.getPropertyPath().toString().split("\\."); // e.g. check.checkRequest.email
            fieldErrors.put(dottedPath[dottedPath.length - 1], v.getMessage());
        }

        Map<String, Object> errorMap = new HashMap<>(
                buildErrorImmutableMap(
                        "Constraint Violations",
                        req.getRequestURI()
                )
        );
        errorMap.put("errors", fieldErrors);
        return errorMap;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> processInputError(MethodArgumentNotValidException exception, HttpServletRequest req) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        Map<String, Object> errorMap = new HashMap<>(
                buildErrorImmutableMap(
                        "Validation Errors",
                        req.getRequestURI()
                )
        );
        errorMap.put("errors", fieldErrors);
        return errorMap;
    }

    /**
     * @return Immutable Collections
     */
    Map<String, Object> buildErrorImmutableMap(String message, String path) {
        return Map.of("timestamp", LocalDateTime.now(),
                "status", BAD_REQUEST.value(),
                "error", BAD_REQUEST.getReasonPhrase(),
                "message", message,
                "path", path);
    }

}
