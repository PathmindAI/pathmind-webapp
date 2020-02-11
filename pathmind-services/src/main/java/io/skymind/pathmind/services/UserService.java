package io.skymind.pathmind.services;

import io.skymind.pathmind.db.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService
{
    public static final String NOT_MATCHING = "* New Password doesn't match Confirmation password";
    public static final String TOO_SHORT = "* 6 min characters";
    public static final String UPPERCASE_MISSING = "* 1 uppercase character";
    public static final String LOWERCASE_MISSING = "* 1 lowercase character";

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
    }

    public List<String> validatePassword(String password, String confirm) {
        List<String> results = new ArrayList<>();

        if (!password.equals(confirm)) {
            results.add(NOT_MATCHING);
        }

        if (password.length() < 6) {
            results.add(TOO_SHORT);
        }

        if (password.chars().filter(ch -> Character.isUpperCase(ch)).findAny().isEmpty()) {
            results.add(UPPERCASE_MISSING);
        }

        if (password.chars().filter(ch -> Character.isLowerCase(ch)).findAny().isEmpty()) {
            results.add(LOWERCASE_MISSING);
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
}
