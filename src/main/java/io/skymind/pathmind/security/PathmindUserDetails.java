package io.skymind.pathmind.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class PathmindUserDetails extends User {

    private long id;
    private String firstname;
    private String lastname;
    private String name; // This is where the names are currently saved

    public PathmindUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, long id, String firstname, String lastname, String name) {
        super(username, password, authorities);
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return getUsername();
    }

    public String getName() {
        return name;
    }
}
