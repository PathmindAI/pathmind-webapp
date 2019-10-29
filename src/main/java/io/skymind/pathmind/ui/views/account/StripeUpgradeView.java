package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.*;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("stripe-upgrade-view")
@JsModule("./src/views/stripe-upgrade-view.js")
@Route(value = "stripe-upgrade-view", layout = MainLayout.class)
public class StripeUpgradeView extends PolymerTemplate<StripeUpgradeView.Model>
		implements HasUrlParameter<String>, AfterNavigationObserver
{

	@Autowired
	private UserService userService;

	private String param;

	@Override
	public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String param) {
		this.param = param;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
	}

	public interface Model extends TemplateModel {
	}
}
