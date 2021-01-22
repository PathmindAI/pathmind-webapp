package io.skymind.pathmind.webapp.ui.views.demo;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;

import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;

public class DemoViewContent extends VerticalLayout {

    private DemoList demoList;

    public DemoViewContent(DemoProjectService demoProjectService, ExperimentManifestRepository experimentManifestRepository, SegmentIntegrator segmentIntegrator) {
        H2 description = new H2("Get started with a pre-configured simulation");
        demoList = new DemoList(demoProjectService, experimentManifestRepository, segmentIntegrator);
        add(description, demoList);
        addClassName("demo-view-content");
    }

    public void setOnChooseDemoHandler(Command handler) {
        demoList.setOnChooseDemoHandler(handler);
    }

}
