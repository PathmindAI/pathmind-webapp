package io.skymind.pathmind.db.testutils;

import java.time.LocalDateTime;
import java.util.UUID;

import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.PathmindUser;

public class UserTestUtils {

    public static final String email1 = "email@email.com";
    public static final String email2 = "email2@email.com";

    public static PathmindUser getNewPathmindUser() {
        PathmindUser pathmindUser = new PathmindUser();
        pathmindUser.setEmail(email1);
        pathmindUser.setPassword("Password");
        pathmindUser.setAccountType(UserRole.Paid.getId());
        pathmindUser.setFirstname("Firstname");
        pathmindUser.setLastname("Lastname");
        pathmindUser.setAddress("Address");
        pathmindUser.setCity("City");
        pathmindUser.setState("State");
        pathmindUser.setCountry("Country");
        pathmindUser.setZip("Zip");
        pathmindUser.setDeleteAt(LocalDateTime.now());
        pathmindUser.setEmailVerifiedAt(LocalDateTime.now());
        pathmindUser.setEmailVerificationToken(UUID.randomUUID());
        return pathmindUser;
    }

    public static void changePathmindUser(PathmindUser pathmindUser) {
        pathmindUser.setEmail(email2);
        pathmindUser.setPassword("Password2");
        pathmindUser.setAccountType(UserRole.Premium.getId());
        pathmindUser.setFirstname("Firstname2");
        pathmindUser.setLastname("Lastname2");
        pathmindUser.setAddress("Address2");
        pathmindUser.setCity("City2");
        pathmindUser.setState("State2");
        pathmindUser.setCountry("Country2");
        pathmindUser.setZip("Zip2");
        pathmindUser.setDeleteAt(LocalDateTime.now());
        pathmindUser.setEmailVerifiedAt(LocalDateTime.now());
        pathmindUser.setEmailVerificationToken(UUID.randomUUID());
    }


}
