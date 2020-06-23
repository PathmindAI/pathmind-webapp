package io.skymind.pathmind.webapp.ui.views.account;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.services.notificationservice.EmailNotificationService;
import io.skymind.pathmind.shared.data.PathmindUser;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.security.UserService;
import io.skymind.pathmind.webapp.ui.binders.PathmindUserBinders;
import io.skymind.pathmind.webapp.ui.utils.ConfirmationUtils;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Tag("account-edit-view-content")
@JsModule("./src/account/account-edit-view-content.js")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccountEditViewContent extends PolymerTemplate<AccountEditViewContent.Model> {

	@Id("lastName")
	public TextField lastName;

	@Id("firstName")
	public TextField firstName;

	@Id("email")
	public TextField email;

	@Id("cancelBtn")
	public Button cancelBtn;

	@Id("updateBtn")
	public Button updateBtn;

	private PathmindUser user;

	private Binder<PathmindUser> binder;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	public AccountEditViewContent(CurrentUser currentUser, UserService userService, EmailNotificationService emailNotificationService,
						   @Value("${pathmind.contact-support.address}") String contactLink) {
		getModel().setContactLink(contactLink);
		user = currentUser.getUser();
		this.userService = userService;
		this.emailNotificationService = emailNotificationService;
		initBinder();

		cancelBtn.addClickShortcut(Key.ESCAPE);

		cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(AccountView.class)));
		updateBtn.addClickListener(e -> {
			if (!FormUtils.isValidForm(binder, user)) {
				return;
			}
			boolean isEmailChanged = !SecurityUtils.getUsername().equals(user.getEmail());
			if (isEmailChanged) {
                ConfirmationUtils.emailUpdateConfirmation(user.getEmail(), () -> updateUserInformation(true)); 
            } else {
                updateUserInformation(false);
            }
		});
	}
	
	private void updateUserInformation(boolean isEmailChanged) {
	    if (isEmailChanged) {
            userService.setNewEmailToVerify(user, SecurityUtils.getUsername(), user.getEmail());
        }
        userService.update(user);

         if (isEmailChanged) {
             emailNotificationService.sendVerificationEmail(user, user.getNewEmailToVerify(), false);
             ConfirmationUtils.emailUpdated(() -> getUI().ifPresent(ui -> ui.getPage().setLocation(Routes.LOGOUT_URL)));
         } else {
             getUI().ifPresent(ui -> ui.navigate(AccountView.class));
         }

     }

	private void initBinder() {
		binder = new Binder<>(PathmindUser.class);

		PathmindUserBinders.bindEmail(userService, binder, email);
		PathmindUserBinders.bindFirstName(binder, firstName);
		PathmindUserBinders.bindLastName(binder, lastName);

		binder.setBean(user);
	}

	public interface Model extends TemplateModel {
		void setContactLink(String contactLink);
	}
}
