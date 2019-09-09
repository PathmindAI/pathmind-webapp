package io.skymind.pathmind.ui.views.errors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.utils.WrapperUtils;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@StyleSheet("frontend://styles/styles.css")
@Route(value="invalidData", layout = MainLayout.class)
public class InvalidDataView extends PathMindDefaultView
{
	public InvalidDataView() {
		super();
	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapCenterFullWidthHorizontal(
				new Label("This link is invalid.\nPlease contact Skymind if you believe this is an error."));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Invalid data error");
	}
}
