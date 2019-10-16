package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Tag("reset-password-view")
@JsModule("./src/account/reset-password-view.js")
@Route(value="reset-password")
public class ResetPasswordView extends PolymerTemplate<TemplateModel>
{
	private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	@Id("email")
	private EmailField emailField;

	@Id("sendBtn")
	private Button sendBtn;

	@Id("cancelBtn")
	private Button cancelBtn;


	@Autowired
	public ResetPasswordView()
	{
	}

	@PostConstruct
	private void init() {
		initContent();
		initBtns();
		emailField.setInvalid(false);
		sendBtn.setEnabled(emailField.isInvalid());

		emailField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
		emailField.addValueChangeListener(e -> {
			boolean valid = isValid(emailField.getValue());
			emailField.setInvalid(!valid);
			sendBtn.setEnabled(valid);
		});
	}

	private void initBtns() {
		sendBtn.addClickListener(e -> {
			if (!emailField.isInvalid()) {
				System.out.println("send email to " + emailField.getValue());
			}
		});
	}

	private void initContent() {
	}

	static boolean isValid(String email) {
		return email.matches(EMAIL_REGEX);
	}
}
