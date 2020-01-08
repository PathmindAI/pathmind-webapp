package io.skymind.pathmind.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@CssImport("./styles/styles.css")
@ParentLayout(MainLayout.class)
public class InvalidDataView extends PathMindDefaultView implements HasErrorParameter<InvalidDataException>
{
	public InvalidDataView() {
		super();
	}
	
	@Override
 	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<InvalidDataException> parameter) {
 		return HttpServletResponse.SC_FORBIDDEN;
 	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapWidthFullCenterHorizontal(
				new Label("This link is invalid.\nPlease contact Skymind if you believe this is an error."));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Invalid data error");
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}
}
