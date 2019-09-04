package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.bus.data.ProjectUpdateStatus;
import io.skymind.pathmind.ui.layouts.MainLayout;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Route(layout = MainLayout.class)
public class MainView extends Composite<Div> implements BeforeEnterObserver
{
	private final UnicastProcessor<ProjectUpdateStatus> publisher;
	private final Flux<ProjectUpdateStatus> consumer;

	public MainView(UnicastProcessor<ProjectUpdateStatus> publisher, Flux<ProjectUpdateStatus> consumer) {
		this.publisher = publisher;
		this.consumer = consumer;
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event)
	{
		 event.rerouteTo("login");
	}
}
