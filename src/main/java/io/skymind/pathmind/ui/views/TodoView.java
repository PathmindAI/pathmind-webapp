package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.layouts.MainLayout;

@Route(value="todo", layout = MainLayout.class)
public class TodoView extends PathMindDefaultView
{
	public TodoView()
	{
		super();
	}

	@Override
	protected Component getTitlePanel() {
		return new Label("TODO");
	}

	@Override
	protected Component getMainContent() {
		return new Label("");
	}

}
