package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Tag("change-password-view-content")
@JsModule("./src/pages/account/change-password-view-content.ts")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChangePasswordViewContent extends LitTemplate {
    @Id("currentPassword")
    private PasswordField currentPassword;

    @Id("newPassword")
    private PasswordField newPassword;

    @Id("confirmNewPassword")
    private PasswordField confirmNewPassword;

    @Id("newPassNotes")
    private VerticalLayout passwordValidationNotes;

    @Id("currentPassNotes")
    private VerticalLayout currentPasswordValidationNotes;

    @Id("cancelBtn")
    private Button cancelBtn;

    @Id("updateBtn")
    private Button updateBtn;

    private PathmindUser user;

    @Autowired
    private UserService userService;

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    @Autowired
    public ChangePasswordViewContent(CurrentUser currentUser) {
        user = currentUser.getUser();

        passwordValidationNotes.setPadding(false);
        passwordValidationNotes.setSpacing(false);
        currentPasswordValidationNotes.setPadding(false);
        currentPasswordValidationNotes.setSpacing(false);

        cancelBtn.addClickShortcut(Key.ESCAPE);

        cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
        updateBtn.addClickListener(e -> {
            if (validate()) {
                if (userService.changePassword(user, newPassword.getValue())) {
                    NotificationUtils.showSuccess("Password was successfully changed.");
                    segmentIntegrator.passwordChanged();
                    getUI().ifPresent(ui -> ui.navigate(AccountView.class));
                } else {
                    NotificationUtils.showError("There was an error during changing password, please try again");
                }
            }
        });
    }

    private void validateCurrentPassword() {
        currentPasswordValidationNotes.removeAll();
        if (!userService.isCurrentPassword(user, currentPassword.getValue())) {
            currentPasswordValidationNotes.removeAll();
            currentPasswordValidationNotes.add(new Span("Password is incorrect"));
            currentPassword.setInvalid(true);
        }
    }

    private void validateNewPassword() {
        passwordValidationNotes.removeAll();
        UserService.PasswordValidationResults validationResults = userService
                .validatePassword(newPassword.getValue(), confirmNewPassword.getValue());
        if (!validationResults.isOk()) {
            newPassword.setInvalid(true);
            passwordValidationNotes.removeAll();
            validationResults.getPasswordValidationErrors().forEach(message -> passwordValidationNotes.add(new Span(message)));
            confirmNewPassword.setInvalid(!validationResults.getConfirmPasswordValidationError().isEmpty());
            confirmNewPassword.setErrorMessage(validationResults.getConfirmPasswordValidationError());
        }
    }

    private boolean validate() {
        validateCurrentPassword();
        validateNewPassword();
        return !currentPassword.isInvalid() && !newPassword.isInvalid();
    }
}
