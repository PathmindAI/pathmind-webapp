package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.layouts.MainLayout;

@Route(layout = MainLayout.class)
public class MainView extends Composite<Div> implements BeforeEnterObserver
{
	// TODO -> Application logo in the browser
	// TODO -> CSS
	// TODO -> Properly handle the login, this is just a quick solution until I know
	// which security framework we use.
	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		 event.rerouteTo("login");
	}
}
