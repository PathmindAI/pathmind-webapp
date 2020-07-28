package io.skymind.pathmind.webapp.ui.layouts.components;

import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.data.Project;
import io.skymind.pathmind.webapp.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.webapp.ui.karibu.KaribuUtils;
import io.skymind.pathmind.webapp.ui.mocks.ProjectMock;
import io.skymind.pathmind.webapp.ui.mocks.ModelMock;
import io.skymind.pathmind.webapp.ui.mocks.ExperimentMock;
import org.mockito.Mockito;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BreadcrumbsTest {

    private UI ui;
    private Project mockProject = new ProjectMock();
    private Model mockModel = new ModelMock();
    private Experiment mockExperiment = new ExperimentMock();
    private static Routes routes;

    @Before
    public void setUp() {
        TestingAuthenticationToken auth = new TestingAuthenticationToken(null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        // routes = new Routes().autoDiscoverViews("io.skymind.pathmind");
        // KaribuUtils.setupRoutes();
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void simpleUITest() {
        final Breadcrumbs breadcrumbs = (Breadcrumbs) UI.getCurrent().getChildren().findFirst().get();
        assertEquals(2, breadcrumbs.getChildren().count());
    }

    @Test
    public void breadcrumbsWithoutRoot() {
        KaribuUtils.setupRoutes(ProjectView.class, ModelView.class);
        Breadcrumbs breadcrumbsWithoutRoot = new Breadcrumbs(mockProject, mockModel, mockExperiment, false);
//        ui = KaribuUtils.setup(breadcrumbsWithoutRoot);
        RouterLink textLink = (RouterLink) breadcrumbsWithoutRoot.getChildren().findFirst().orElse(null);
        assertNotEquals("Projects", textLink.getText());
    }

    @Test
    public void breadcrumbsWithRoot() {
        // Breadcrumbs breadcrumbsWithoutRoot = new Breadcrumbs(mockProject, mockModel, mockExperiment);
    }

    @Test
    public void singleBreadcrumb() {
        Breadcrumbs singleBreadcrumb = new Breadcrumbs("Account");
        ui = KaribuUtils.setup(singleBreadcrumb);
        Span textSpan = (Span) singleBreadcrumb.getChildren().findFirst().orElse(null);
        assertEquals("Account", textSpan.getText());
    }

    @Test
    public void showModelPackageName() {
        // Breadcrumbs breadcrumbsWithoutRoot = new Breadcrumbs(mockProject, mockModel, mockExperiment);
        // ("modelMock_v1")
    }
}