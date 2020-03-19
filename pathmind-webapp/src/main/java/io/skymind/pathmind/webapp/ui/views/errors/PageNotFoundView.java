package io.skymind.pathmind.webapp.ui.views.errors;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;

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
				LabelFactory.createLabel("The page you requested could not be found. Please contact Pathmind for assistance."));
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}
}