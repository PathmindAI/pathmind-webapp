package io.skymind.pathmind.webapp.ui.views.errors;

import io.skymind.pathmind.webapp.exception.InvalidDataException;
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
public class InvalidDataView extends PathMindDefaultView implements HasErrorParameter<InvalidDataException>
{
	private String errorId;
	
	public InvalidDataView() {
		super();
	}
	
	@Override
 	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<InvalidDataException> parameter) {
 		errorId = PathmindErrorHandler.generateUniqueErrorId();
 		log.error(String.format("Error #%s: %s", errorId, parameter.getException().getMessage()), parameter.getException());
		return HttpServletResponse.SC_FORBIDDEN;
 	}

	@Override
	protected Component getMainContent() {
		return WrapperUtils.wrapWidthFullCenterVertical(
				LabelFactory.createLabel(String.format("This link is invalid. Please contact Pathmind if you believe this is an error (#%s).", errorId)));
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
