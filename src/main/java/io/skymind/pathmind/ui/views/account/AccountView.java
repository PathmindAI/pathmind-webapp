package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.layouts.MainLayout;

@Route(value="account", layout = MainLayout.class)
public class AccountView extends VerticalLayout
{
	public AccountView()
	{
		add(new Label("TODO -> Account"));
	}
}
