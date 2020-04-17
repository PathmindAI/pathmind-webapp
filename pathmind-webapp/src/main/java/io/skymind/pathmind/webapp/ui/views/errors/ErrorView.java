package io.skymind.pathmind.webapp.ui.views.errors;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
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
		return new ScreenTitlePanel("Please contact Pathmind for assistance.");
	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapWidthFullCenterVertical(
				LabelFactory.createLabel(String.format("An unexpected error occurred. Please contact Pathmind for assistance (#%s).", errorId)));
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return true;
	}
}
