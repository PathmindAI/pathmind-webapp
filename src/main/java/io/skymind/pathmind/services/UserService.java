package io.skymind.pathmind.services;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService
{

    private static Logger log = LogManager.getLogger(UserService.class);


    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO)
    {
        this.userDAO = userDAO;
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
            results.add("* New Password doesn't match Confirmation password");
        }

        if (password.length() < 6) {
            results.add("* 6 min characters");
        }

        if (password.chars().filter(ch -> Character.isUpperCase(ch)).findAny().isEmpty()) {
            results.add("* 1 uppercase character");
        }

        if (password.chars().filter(ch -> Character.isLowerCase(ch)).findAny().isEmpty()) {
            results.add("* 1 lowercase character");
        }

        return results;
    }

    public boolean isCurrentPassword(PathmindUser user, String password) {
//        TODO encrypt
        return user.getPassword().equals(password);
    }
}
