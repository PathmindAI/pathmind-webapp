package io.skymind.pathmind.webapp.ui.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import io.skymind.pathmind.shared.security.Routes;

import static io.skymind.pathmind.shared.security.Routes.ROOT_URL;

@Route(ROOT_URL)
public class MainView extends Composite<Div> implements BeforeEnterObserver
{
	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		 event.rerouteTo(Routes.LOGIN_URL);
	}
}