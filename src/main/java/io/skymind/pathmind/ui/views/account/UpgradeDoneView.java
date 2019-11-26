package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.services.UserService;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static io.skymind.pathmind.security.Routes.UPGRADE_DONE;

@Route(value = UPGRADE_DONE, layout = MainLayout.class)
public class UpgradeDoneView extends PathMindDefaultView
{

	private final UpgradeDoneViewContent upgradeDoneViewContent;
	
	@Autowired
	public UpgradeDoneView(UpgradeDoneViewContent upgradeDoneViewContent) {
		this.upgradeDoneViewContent = upgradeDoneViewContent;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("UPGRADE", "Subscription Plans");
	}

	@Override
	protected Component getMainContent() {
		return upgradeDoneViewContent;
	}

}
