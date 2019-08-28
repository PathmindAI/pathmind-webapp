package io.skymind.pathmind.ui.layouts;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.page.Push;
import io.skymind.pathmind.ui.layouts.components.AccountHeaderPanel;
import io.skymind.pathmind.ui.layouts.components.SectionsHeaderPanel;

// TODO -> Do we want it on the mainLayout or should we move it to a PushLayout which extends MainLayout
// so that we don't have push everywhere. Or maybe do we need push everywhere because running experiments
// can push updates to the existing screens at any time through an eventbus?
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
