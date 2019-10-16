package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Tag("reset-password-view")
@JsModule("./src/account/reset-password-view.js")
@Route(value="reset-password", layout = MainLayout.class)
public class ResetPasswordView extends PolymerTemplate<TemplateModel>
{


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
	}

	private void initBtns() {
	}

	private void initContent() {
	}

}
