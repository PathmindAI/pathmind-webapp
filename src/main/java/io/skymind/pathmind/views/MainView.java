package io.skymind.pathmind.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout implements BeforeEnterObserver
{
	public MainView()
	{
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		 event.rerouteTo("login");
	}
}
