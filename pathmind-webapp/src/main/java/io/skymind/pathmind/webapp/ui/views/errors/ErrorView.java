package io.skymind.pathmind.webapp.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@CssImport("./styles/styles.css")
@ParentLayout(MainLayout.class)
@Slf4j
public class ErrorView extends PathMindDefaultView implements HasErrorParameter<Exception>
{
    @Autowired
    private SegmentIntegrator segmentIntegrator;
    
    private String errorId;

    public ErrorView() {
        super();
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
        errorId = PathmindErrorHandler.generateUniqueErrorId();
        log.error(String.format("Error #%s: %s", errorId, parameter.getException().getMessage()), parameter.getException());
        segmentIntegrator.errorPageDisplayed(event.getLocation().getPath(), parameter.getException().toString());
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    @Override
    protected Component getTitlePanel() {
        return new ScreenTitlePanel("Oops!");
    }

    @Override
    protected Component getMainContent() {
        return WrapperUtils.wrapWidthFullCenterVertical(
                LabelFactory.createLabel("An unexpected error occurred. Try signing out of Pathmind and signing back in."),
                LabelFactory.createLabel(String.format("If you still see this error, please contact Pathmind for assistance (#%s).", errorId), CssPathmindStyles.NO_TOP_MARGIN_LABEL),
                StatusPageMessage.getMessage()
        );
    }
}
