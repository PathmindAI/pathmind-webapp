package io.skymind.pathmind.webapp.ui.views.model;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;

@Route(value = Routes.UPLOAD_MODEL_ERROR, layout = MainLayout.class)
public class UploadModelErrorView extends PathMindDefaultView implements HasUrlParameter<String> {

    @Autowired
    private SegmentIntegrator segmentIntegrator;

    private String contactLink;
    private String errorMessage;
    private UploadModelErrorViewContent viewContent;

    public UploadModelErrorView(@Value("${pathmind.contact-support.address}") String contactLink) {
        super();
        this.contactLink = contactLink;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        segmentIntegrator.uploadModelError();
    }

    @Override
    protected Component getMainContent() {
        viewContent = new UploadModelErrorViewContent(contactLink);
        viewContent.setError(errorMessage);
        return viewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String errorMessage) {
        if (errorMessage != null) {
            this.errorMessage = errorMessage;
        }
    }
}
