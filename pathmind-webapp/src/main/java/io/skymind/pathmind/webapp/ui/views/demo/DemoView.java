package io.skymind.pathmind.webapp.ui.views.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;

@Route(value = Routes.DEMOS_URL, layout = MainLayout.class)
public class DemoView extends PathMindDefaultView {
    @Autowired
    private DemoProjectService demoProjectService;
    @Autowired
    private ExperimentManifestRepository experimentManifestRepository;
    @Autowired
    private SegmentIntegrator segmentIntegrator;

    public DemoView() {
        super();
    }

    @Override
    protected Component getMainContent() {
        DemoViewContent demoViewContent = new DemoViewContent(demoProjectService, experimentManifestRepository, segmentIntegrator);
        return demoViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }
}
