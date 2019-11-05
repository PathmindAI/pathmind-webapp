package io.skymind.pathmind.ui.views.errors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.utils.WrapperUtils;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@CssImport("./styles/styles.css")
@Route(value = Routes.ERROR_URL, layout = MainLayout.class)
public class ErrorView extends PathMindDefaultView
{
	public ErrorView() {
		super();
	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapWidthFullCenterVertical(
				new Label("An unexpected error occurred. Please contact Skymind for assistance."));
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}
}
