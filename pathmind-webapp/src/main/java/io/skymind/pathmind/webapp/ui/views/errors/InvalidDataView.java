package io.skymind.pathmind.webapp.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@CssImport("./styles/styles.css")
@ParentLayout(MainLayout.class)
@Slf4j
public class InvalidDataView extends PathMindDefaultView implements HasErrorParameter<InvalidDataException> {
    private String errorId;
    private String errorMessage;

    public InvalidDataView() {
        super();
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<InvalidDataException> parameter) {
        errorId = PathmindErrorHandler.generateUniqueErrorId();
        errorMessage = parameter.getException().getMessage();
        log.error(String.format("Error #%s: %s", errorId, errorMessage), parameter.getException());
        return HttpServletResponse.SC_FORBIDDEN;
    }

    @Override
    protected Component getMainContent() {
        return WrapperUtils.wrapWidthFullCenterVertical(
                new H3("Invalid Data Error"),
                new Span(errorMessage),
                new Span(String.format("This link is invalid. Please contact Pathmind if you believe this is an error (#%s).", errorId)),
                StatusPageMessage.getMessage()
        );
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }
}
