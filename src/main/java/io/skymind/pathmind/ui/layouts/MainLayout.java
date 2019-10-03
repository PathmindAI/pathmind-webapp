package io.skymind.pathmind.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.HtmlImport;
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
@StyleSheet("frontend://styles/styles.css")
@Theme(Lumo.class)
@HtmlImport("frontend://styles/shared-styles.html")
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
