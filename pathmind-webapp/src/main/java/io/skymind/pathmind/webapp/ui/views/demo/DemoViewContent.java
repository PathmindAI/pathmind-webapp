package io.skymind.pathmind.webapp.ui.views.demo;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;

public class DemoViewContent extends VerticalLayout {

    public DemoViewContent(DemoProjectService demoProjectService, ExperimentManifestRepository experimentManifestRepository) {
        H2 description = new H2("Get started with a pre-configured simulation");
        DemoList demoList = new DemoList(demoProjectService, experimentManifestRepository);
        add(description, demoList);
        addClassName("demo-view-content");
    }

}
