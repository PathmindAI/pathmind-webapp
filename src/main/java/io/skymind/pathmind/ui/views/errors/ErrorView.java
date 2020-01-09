package io.skymind.pathmind.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@CssImport("./styles/styles.css")
@ParentLayout(MainLayout.class)
@Slf4j
public class ErrorView extends PathMindDefaultView implements HasErrorParameter<Exception>
{
	private String errorId;
	
	public ErrorView() {
		super();
	}
	
	@Override
 	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
		errorId = PathmindErrorHandler.generateUniqueErrorId();
 		log.error(String.format("Error #%s: %s", errorId, parameter.getException().getMessage()), parameter.getException());
 		return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
 	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("Please contact Skymind for assistance.");
	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapWidthFullCenterVertical(
				new Label(String.format("An unexpected error occurred. Please contact Skymind for assistance (#%s).", errorId)));
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}
}
