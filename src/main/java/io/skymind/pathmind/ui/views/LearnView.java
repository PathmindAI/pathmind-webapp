package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.layouts.MainLayout;

@Route(value="learn", layout = MainLayout.class)
public class LearnView extends PathMindDefaultView
{
	public LearnView()
	{
		super();
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("TODO -> Implement")
		);
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
