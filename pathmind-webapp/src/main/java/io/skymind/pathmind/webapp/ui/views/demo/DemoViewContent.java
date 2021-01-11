package io.skymind.pathmind.webapp.ui.views.demo;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;

public class DemoViewContent extends VerticalLayout {

    public DemoViewContent(DemoProjectService demoProjectService, ExperimentManifestRepository experimentManifestRepository) {
        Paragraph description = new Paragraph(
            new Span("We have prepared some demos for you."),
            new Html("<br>"),
            new Span("Choose a demo from the following list to start experiencing Pathmind."));
        DemoList demoList = new DemoList(demoProjectService, experimentManifestRepository);
        add(description, demoList);
        addClassName("demo-view-content");
    }

}
