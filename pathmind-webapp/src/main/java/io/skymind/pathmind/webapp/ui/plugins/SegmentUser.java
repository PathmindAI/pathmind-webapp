package io.skymind.pathmind.webapp.ui.plugins;

import io.skymind.pathmind.shared.security.PathmindUserDetails;

/**
 * Contains user information that is passed to client side and used in identify
 * calls in segment
 */
public class SegmentUser {
    private String id;
    private String name;
    private String email;

    public SegmentUser(PathmindUserDetails userDetails) {
        id = Long.toString(userDetails.getId());
        name = userDetails.getName();
        email = userDetails.getEmail();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
