package io.skymind.pathmind.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import io.skymind.pathmind.ui.layouts.components.AccountHeaderPanel;
import io.skymind.pathmind.ui.layouts.components.SectionsHeaderPanel;

@Push
public class MainLayout extends AppLayout
{
	public MainLayout()
	{
		addToNavbar(new SectionsHeaderPanel(), new AccountHeaderPanel());

		// Added a message just in case there's ever a failure.
		setContent(new Span("Error. Please contact Skymind for assistance"));
	}
}
