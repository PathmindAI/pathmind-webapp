package io.skymind.pathmind.ui.views.account;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Tag("account-view")
@JsModule("src/account/account-view.js")
@Route(value="account", layout = MainLayout.class)
public class AccountView extends PolymerTemplate<TemplateModel>
{
	private PathmindUser user;

	@Autowired
	public AccountView(CurrentUser currentUser)
	{
		user = currentUser.getUser();

	}

	@PostConstruct
	private void init() {
//		addClassName("account-view");
		initTabs();
		initContent();
//		add(new Label("TODO -> Account for " + user.getEmail()));
	}

	private void initContent() {
		Span userTitle = new Span("User Email");
		userTitle.addClassName("title");
		Span userInfo = new Span(user.getEmail());
		userInfo.addClassName("info");


		VerticalLayout basicInfoContent = WrapperUtils.wrapWidthFullVertical(userTitle, userInfo);
		Button edit = new Button("Edit");
		edit.addClassName("edit");
		HorizontalLayout basicInfo = WrapperUtils.wrapWidthFullHorizontal(basicInfoContent, edit);


		VerticalLayout container = WrapperUtils.wrapWidthFullVertical(basicInfo);
//		add(container);
	}

	private void initTabs() {
		Tabs tabs = new Tabs();
		tabs.add(new Tab("Account information"));
		tabs.setWidth("100%");
	}
}
