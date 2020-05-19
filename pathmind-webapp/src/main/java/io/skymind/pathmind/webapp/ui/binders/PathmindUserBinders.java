package io.skymind.pathmind.webapp.ui.binders;

import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.ui.converter.TrimmedStringConverter;

public class PathmindUserBinders {

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

    public static void bindEmail(Binder<PathmindUser> binder, TextField field) {
        binder.forField(field)
                .withConverter(new TrimmedStringConverter())
                .asRequired("Email is required")
                .withValidator(new EmailValidator(
                "This doesn't look like a valid email address"))
                .bind(PathmindUser::getEmail, PathmindUser::setEmail);

    }
}
