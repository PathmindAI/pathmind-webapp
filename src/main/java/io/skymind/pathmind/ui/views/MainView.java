package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("/")
public class MainView extends Composite<Div> implements BeforeEnterObserver
{
	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		 event.rerouteTo("login");
	}
}