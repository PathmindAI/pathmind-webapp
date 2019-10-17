package io.skymind.pathmind.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.skymind.pathmind.ui.layouts.components.AccountHeaderPanel;
import io.skymind.pathmind.ui.layouts.components.SectionsHeaderPanel;
import io.skymind.pathmind.ui.utils.VaadinUtils;

@Push(PushMode.AUTOMATIC)
@CssImport(value = "./styles/styles.css", id = "shared-styles")
@CssImport(value = "./styles/components/vaadin-text-field.css", themeFor = "vaadin-text-field")
@CssImport(value = "./styles/components/vaadin-button.css", themeFor = "vaadin-button")
@CssImport(value = "./styles/components/vaadin-grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./styles/components/vaadin-split-layout.css", themeFor = "vaadin-split-layout")
@CssImport(value = "./styles/components/vaadin-chart.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
@CssImport(value = "./styles/components/vaadin-form-item.css", themeFor = "vaadin-form-item")
@CssImport(value = "./styles/layouts/vaadin-app-layout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "./styles/views/experiment-view.css")
@CssImport(value = "./styles/views/experiments-view.css")
@CssImport(value = "./styles/views/new-experiment-view.css")
@CssImport(value = "./styles/views/login-view-styles.css", id = "login-view-styles")
@CssImport(value = "./styles/views/sign-up-view.css", id = "sign-up-view-styles")
@Theme(Lumo.class)
public class MainLayout extends AppLayout implements PageConfigurator
{
	public MainLayout()
	{
		setId("pathmind-app-layout");
		addToNavbar(new SectionsHeaderPanel(), new AccountHeaderPanel());


		// Added a message just in case there's ever a failure.
		setContent(new Span("Error. Please contact Skymind for assistance"));
	}


	@Override
	public void configurePage(InitialPageSettings settings) {
		VaadinUtils.setupFavIcon(settings);
	}
}
