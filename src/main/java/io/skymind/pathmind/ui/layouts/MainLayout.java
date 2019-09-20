package io.skymind.pathmind.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import io.skymind.pathmind.ui.layouts.components.AccountHeaderPanel;
import io.skymind.pathmind.ui.layouts.components.SectionsHeaderPanel;

@Push
@StyleSheet("frontend://styles/styles.css")
@Theme(Lumo.class)
@HtmlImport("frontend://styles/shared-styles.html")
public class MainLayout extends AppLayout
{
	public MainLayout()
	{
		setId("pathmind-app-layout");
		addToNavbar(new SectionsHeaderPanel(), new AccountHeaderPanel());


		// Added a message just in case there's ever a failure.
		setContent(new Span("Error. Please contact Skymind for assistance"));
	}
}
