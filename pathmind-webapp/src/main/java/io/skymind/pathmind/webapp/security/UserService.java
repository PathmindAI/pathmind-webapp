package io.skymind.pathmind.webapp.security;

import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.UserUpdateBusEvent;
import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService
{
    public static final String NOT_MATCHING = "* New Password doesn't match Confirmation password";
    public static final String TOO_SHORT = "* 6 min characters";
    public static final String TOO_BIG = "* 50 max characters";
    public static final String UPPERCASE_MISSING = "* 1 uppercase character";
    public static final String LOWERCASE_MISSING = "* 1 lowercase character";
    public static final String CONFIRMATION_REQUIRED = "Confirmation is required";

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDAO, PasswordEncoder passwordEncoder)
    {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public PathmindUser getCurrentUser()
    {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
            return userDAO.findByEmailIgnoreCase(userDetails.getUsername());
        }
        return null;
    }

    public long getCurrentUserId()
    {
        final PathmindUser user = getCurrentUser();
        if (user == null){
            throw new RuntimeException(new AuthenticationException("User not authenticated!"));
        } else{
            return user.getId();
        }
    }

    public boolean isCurrentUserVerified() {
        return null != getCurrentUser() && (null != getCurrentUser().getEmailVerifiedAt());
    }

    public PathmindUser signup(PathmindUser pathmindUser)
    {
        pathmindUser.setEmailVerificationToken(UUID.randomUUID());
        long id = userDAO.insertUser(pathmindUser);
        log.info("New user signed up: " + pathmindUser.getEmail());
        return userDAO.findById(id);
    }

    public boolean verifyAccount(PathmindUser pathmindUser, UUID verificationUUID) {
        if (null == verificationUUID) {
            return false;
        }
        if (pathmindUser.getEmailVerificationToken().equals(verificationUUID)) {
            setUserVerified(pathmindUser);
            return true;
        }
        return false;
    }

    private void setUserVerified(PathmindUser pathmindUser) {
        pathmindUser.setEmailVerifiedAt(LocalDateTime.now());
    }

    public void update(PathmindUser user) {
        userDAO.update(user);
        EventBus.post(new UserUpdateBusEvent(user));
    }

    public PasswordValidationResults validatePassword(String password, String confirm) {
        PasswordValidationResults results = new PasswordValidationResults();

        if (confirm.isEmpty()) {
            results.setConfirmPasswordValidationError(CONFIRMATION_REQUIRED);
        }
        else if (!password.equals(confirm)) {
            results.getPasswordValidationErrors().add(NOT_MATCHING);
        }

        if (password.length() < 6) {
            results.getPasswordValidationErrors().add(TOO_SHORT);
        }

        if (password.length() > 50) {
            results.getPasswordValidationErrors().add(TOO_BIG);
        }

        if (password.chars().filter(ch -> Character.isUpperCase(ch)).findAny().isEmpty()) {
            results.getPasswordValidationErrors().add(UPPERCASE_MISSING);
        }

        if (password.chars().filter(ch -> Character.isLowerCase(ch)).findAny().isEmpty()) {
            results.getPasswordValidationErrors().add(LOWERCASE_MISSING);
        }

        return results;
    }

    public boolean isCurrentPassword(PathmindUser user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public boolean changePassword(PathmindUser user, String password) {
        return userDAO.changePassword(user.getId(), password);
    }

    public PathmindUser findByEmailIgnoreCase(String value) {
        return userDAO.findByEmailIgnoreCase(value);
    }
    public PathmindUser findByToken(String value) {
        return userDAO.findByToken(value);
    }

    public static class PasswordValidationResults {
        private Collection<String> passwordValidationErrors = new ArrayList<>();
        private String confirmPasswordValidationError = "";

        public Collection<String> getPasswordValidationErrors() {
            return passwordValidationErrors;
        }

        public String getConfirmPasswordValidationError() {
            return confirmPasswordValidationError;
        }

        public void setConfirmPasswordValidationError(String confirmPasswordValidationError) {
            this.confirmPasswordValidationError = confirmPasswordValidationError;
        }

        public boolean isOk() {
            return passwordValidationErrors.isEmpty() && confirmPasswordValidationError.isEmpty();
        }
    }


    /*
     * When user wants to change email, we keep this email in a temporary field till it's verified.
     * For the new address, a new verification token is generated, so that previous one will not work.
     * Once user clicks the verify link in verification mail, verifyEmailByToken method replaces Email with NewEmailToVerify field
     */
    public void setNewEmailToVerify(PathmindUser user, String originalEmail, String newEmail) {
        user.setEmail(originalEmail);
        user.setNewEmailToVerify(newEmail);
        user.setEmailVerificationToken(UUID.randomUUID());
    }

    // Verifies the email for token, and returns PathmindUser instance
    // If token is not valid, returns null
    public PathmindUser verifyEmailByToken(String token) {
        PathmindUser user = findByToken(token);
        if (user != null) {
            user.setEmailVerifiedAt(LocalDateTime.now());
            if (Strings.isNotBlank(user.getNewEmailToVerify())) {
                user.setEmail(user.getNewEmailToVerify());
                user.setNewEmailToVerify(null);
            }
        }
        update(user);
        return user;
    }
}
