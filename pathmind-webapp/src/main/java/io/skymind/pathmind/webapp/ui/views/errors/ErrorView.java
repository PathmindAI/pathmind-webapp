package io.skymind.pathmind.webapp.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
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
import io.skymind.pathmind.webapp.utils.CookieUtils;
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
        Button signOutButton = new Button("signing out");
        signOutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        signOutButton.addClickListener(evt -> {
            getUI().ifPresent(ui -> {
                CookieUtils.deleteAWSCanCookie();
                ui.getSession().getSession().invalidate();
                ui.getPage().reload(); 
            });
        });
        
        return WrapperUtils.wrapWidthFullCenterVertical(
                new Span(new Text("An unexpected error occurred. Try "), signOutButton, new Text(" of Pathmind and signing back in.")),
                LabelFactory.createLabel(String.format("If you still see this error, please contact Pathmind for assistance (#%s).", errorId), CssPathmindStyles.NO_TOP_MARGIN_LABEL),
                StatusPageMessage.getMessage()
        );
    }
}
