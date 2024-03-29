package io.skymind.pathmind.webapp.ui.views.errors;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.VaadinUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO -> Implement correctly, this is just a quick stub.
 */
@ParentLayout(MainLayout.class)
@Slf4j
public class ErrorView extends PathMindDefaultView implements HasErrorParameter<Exception> {
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
        return null;
    }

    @Override
    protected Component getMainContent() {
        Button signOutButton = new Button("signing out");
        signOutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        signOutButton.addClickListener(evt -> {
            getUI().ifPresent(ui -> VaadinUtils.signout(ui, true));
        });

        return WrapperUtils.wrapWidthFullCenterVertical(
                new H3("Oops!"),
                new Span(new Text("An unexpected error occurred. Try "), signOutButton, new Text(" of Pathmind and signing back in.")),
                new Span(String.format("If you still see this error, please contact Pathmind for assistance (#%s).", errorId)),
                StatusPageMessage.getMessage()
        );
    }
}
