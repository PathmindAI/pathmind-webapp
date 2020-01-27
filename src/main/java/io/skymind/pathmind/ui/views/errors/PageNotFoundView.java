package io.skymind.pathmind.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;

import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;

@CssImport("./styles/styles.css")
@ParentLayout(MainLayout.class)
public class PageNotFoundView extends PathMindDefaultView implements HasErrorParameter<NotFoundException>
{
	public PageNotFoundView() {
		super();
	}

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		return HttpServletResponse.SC_NOT_FOUND;
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Page not found");
	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapWidthFullCenterVertical(
				new Label("The page you requested could not be found. Please contact Pathmind for assistance."));
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}
}