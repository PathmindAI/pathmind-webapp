package io.skymind.pathmind.ui.views.learn;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@Route(value="learn", layout = MainLayout.class)
public class LearnView extends PathMindDefaultView
{
	public LearnView()
	{
		super();
	}

	@Override
	protected Component getTitlePanel() {
		return new Label("TODO");
	}

	@Override
	protected Component getMainContent() {
		return new Label("TODO");
	}

}
