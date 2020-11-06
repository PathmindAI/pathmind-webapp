package io.skymind.pathmind.webapp.ui.binders;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.converter.TrimmedStringConverter;

public class PathmindUserBinders {
    
    private static final String EMAIL_IS_USED = "This email is already used";

    public static void bindFirstName(Binder<PathmindUser> binder, TextField field) {
        binder.forField(field)
                .withConverter(new TrimmedStringConverter())
                .asRequired("First Name is required")
                .withValidator(new StringLengthValidator("First Name must not exceed 250 characters", 0, 250))
                .bind(PathmindUser::getFirstname, PathmindUser::setFirstname);
    }

    public static void bindLastName(Binder<PathmindUser> binder, TextField field) {
        binder.forField(field)
                .withConverter(new TrimmedStringConverter())
                .asRequired("Last Name is required")
                .withValidator(new StringLengthValidator("Last Name must not exceed 250 characters", 0, 250))
                .bind(PathmindUser::getLastname, PathmindUser::setLastname);
    }

    public static void bindEmail(UserService userService, Binder<PathmindUser> binder, TextField field) {
        binder.forField(field)
                .withConverter(new TrimmedStringConverter())
                .asRequired("Email is required")
                .withValidator(new EmailValidator("This doesn't look like a valid email address"))
                .withValidator(email -> verifyEmailAddressIsNotUsed(userService, email), EMAIL_IS_USED)
                .bind(PathmindUser::getEmail, PathmindUser::setEmail);

    }

    private static boolean verifyEmailAddressIsNotUsed(UserService userService, String email) {
        PathmindUser user = userService.findByEmailIgnoreCase(email);
        if (user == null) {
            return true;
        }
        if (SecurityUtils.isUserLoggedIn() && user.getId() == SecurityUtils.getUserId()) {
            return true;
        } else {
            return false;
        }
    }
}
