package io.skymind.pathmind.services;

import javax.naming.AuthenticationException;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PathmindUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
            return userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        }
        return null;
    }

    public long getCurrentUserId() {
        final PathmindUser user = getCurrentUser();
        if (user == null){
            throw new RuntimeException(new AuthenticationException("User not authenticated!"));
        } else{
            return user.getId();
        }
    }


}
