package io.skymind.pathmind.webapp.ui.layouts.components;

import java.util.stream.Collectors;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;
import org.junit.Test;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.karibu.KaribuUtils;
import io.skymind.pathmind.webapp.ui.mocks.ProjectMock;
import io.skymind.pathmind.webapp.ui.mocks.ModelMock;
import io.skymind.pathmind.webapp.ui.mocks.ExperimentMock;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

public class BreadcrumbsTest {

    private Project mockProject = new ProjectMock();
    private Model mockModel = new ModelMock();
    private Experiment mockExperiment = new ExperimentMock();

    @Test
    public void breadcrumbsWithoutRoot() {
        KaribuUtils.setupRoutes(ProjectView.class, ModelView.class);
        Breadcrumbs breadcrumbsWithoutRoot = new Breadcrumbs(mockProject, mockModel, mockExperiment, false);
        RouterLink textLink = (RouterLink) breadcrumbsWithoutRoot.getChildren().findFirst().orElse(null);
        assertNotEquals("Projects", textLink.getText());
        assertEquals(mockProject.getName(), textLink.getText());
    }

    @Test
    public void breadcrumbsWithRoot() {
        KaribuUtils.setupRoutes(ProjectsView.class, ProjectView.class, ModelView.class);
        Breadcrumbs breadcrumbsWithoutRoot = new Breadcrumbs(mockProject, mockModel, mockExperiment);
        RouterLink textLink = (RouterLink) breadcrumbsWithoutRoot.getChildren().findFirst().orElse(null);
        assertEquals("Projects", textLink.getText());
    }

    @Test
    public void singleBreadcrumb() {
        Breadcrumbs singleBreadcrumb = new Breadcrumbs("Account");
        Span textSpan = (Span) singleBreadcrumb.getChildren().findFirst().orElse(null);
        assertEquals("Account", textSpan.getText());
    }

    @Test
    public void showModelPackageName() {
        KaribuUtils.setupRoutes(ProjectsView.class, ProjectView.class, ModelView.class);
        Breadcrumbs breadcrumbsWithoutRoot = new Breadcrumbs(mockProject, mockModel, mockExperiment);
        RouterLink modelTextLink = (RouterLink) breadcrumbsWithoutRoot.getChildren().filter(child -> !child.getElement().getText().equals(">")).collect(Collectors.toList()).get(2);
        assertThat(modelTextLink.getText(), containsString("(modelMock_v1)"));
    }
}