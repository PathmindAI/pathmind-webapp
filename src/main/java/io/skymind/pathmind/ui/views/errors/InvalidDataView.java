package io.skymind.pathmind.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.LabelFactory;
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
