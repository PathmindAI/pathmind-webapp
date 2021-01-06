package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.skymind.pathmind.shared.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathmindUser {
    private long id;
    private String email;
    private String password;
    private int accountType;
    private String firstname;
    private String lastname;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zip;
    private LocalDateTime deleteAt;
    private LocalDateTime emailVerifiedAt;
    private UUID emailVerificationToken;
    private LocalDateTime passwordResetSendAt;
    private String stripeCustomerId;
    private String newEmailToVerify;
    private String apiKey;
    private LocalDateTime apiKeyCreatedAt;

    private List<Project> projects;

    public String getName() {
        return firstname + " " + lastname;
    }

    public UserRole getAccountType() {
        return UserRole.getEnumFromId(this.accountType);
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public boolean isSupportAccountType() {
        return UserRole.Support.equals(getAccountType());
    }
}