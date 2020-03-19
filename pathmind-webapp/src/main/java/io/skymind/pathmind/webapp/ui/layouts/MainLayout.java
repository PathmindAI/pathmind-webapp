package io.skymind.pathmind.webapp.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import io.skymind.pathmind.webapp.security.CurrentUser;
import io.skymind.pathmind.webapp.security.FeatureManager;
import io.skymind.pathmind.webapp.ui.layouts.components.AccountHeaderPanel;
import io.skymind.pathmind.webapp.ui.layouts.components.SectionsHeaderPanel;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;

@Push(PushMode.AUTOMATIC)
@CssImport(value = "./styles/styles.css", id = "shared-styles")
@CssImport(value = "./styles/components/vaadin-text-field.css", themeFor = "vaadin-text-field")
@CssImport(value = "./styles/components/vaadin-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/vaadin-menu-bar.css", themeFor = "vaadin-menu-bar")
@CssImport(value = "./styles/components/vaadin-context-menu-list-box.css", themeFor = "vaadin-context-menu-list-box")
@CssImport(value = "./styles/components/vaadin-grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/components/vaadin-split-layout.css", themeFor = "vaadin-split-layout")
@CssImport(value = "./styles/components/vaadin-chart.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
@CssImport(value = "./styles/components/vaadin-form-item.css", themeFor = "vaadin-form-item")
@CssImport(value = "./styles/components/vaadin-tabs.css", themeFor = "vaadin-tabs")
@CssImport(value = "./styles/components/vaadin-tab.css", themeFor = "vaadin-tab")
@CssImport(value = "./styles/components/vaadin-text-area.css", themeFor = "vaadin-text-area")
@CssImport(value = "./styles/components/override/vaadin-grid.css")
@CssImport(value = "./styles/layouts/vaadin-app-layout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "./styles/views/experiment-view.css")
@CssImport(value = "./styles/views/dashboard-view.css")
@CssImport(value = "./styles/views/pathmind-dialog-view.css", id = "pathmind-dialog-view")
@CssImport(value = "./styles/views/guide-view.css", id = "guide-view")
// Stripe should be added to every page to be able to use their fraud detection mechanism
@JavaScript("https://js.stripe.com/v3/")
@Theme(Lumo.class)
public class MainLayout extends AppLayout implements PageConfigurator
{

	public MainLayout(CurrentUser user, FeatureManager featureManager)
	{
		setId("pathmind-app-layout");
		boolean hasLoginUser = user != null && user.getUser() != null;
		addToNavbar(new SectionsHeaderPanel(hasLoginUser, featureManager));
		if (hasLoginUser) {
			addToNavbar(new AccountHeaderPanel(user.getUser()));
		}

		// Added a message just in case there's ever a failure.
		setContent(new Span("Error. Please contact Skymind for assistance"));
	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		VaadinUtils.setupFavIcon(settings);
	}
}
