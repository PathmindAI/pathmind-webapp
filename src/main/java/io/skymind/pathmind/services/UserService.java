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

}
