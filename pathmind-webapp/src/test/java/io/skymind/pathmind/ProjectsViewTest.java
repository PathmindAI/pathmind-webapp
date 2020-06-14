package io.skymind.pathmind;

import com.github.mvysny.kaributesting.v10.GridKt;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import io.skymind.pathmind.webapp.AbstractKaribuIntegrationTest;
import io.skymind.pathmind.webapp.ui.views.project.NewProjectView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.Assert.assertSame;

@WithUserDetails("steph@followsteph.com")
@Transactional
public class ProjectsViewTest extends AbstractKaribuIntegrationTest {

    @Before
    public void setUp() throws Exception {
        UI.getCurrent().navigate(ProjectsView.class);
    }

    private void assertSelectedTabSheetTab(String caption) {
        assertSame(_get(Tab.class, tabSearchSpecJ -> tabSearchSpecJ.withText(caption)), _get(Tabs.class).getSelectedTab());
    }

    @Test
    public void activeTabContainsActiveProjects() {
        assertSelectedTabSheetTab("Active");

        // this just count from my local database
        GridKt.expectRows(_get(Grid.class), 1);
    }

    @Test
    public void archivesTabContainsArhivedProjects() {
        _get(Tabs.class).setSelectedTab(_get(Tab.class, tabSearchSpecJ -> tabSearchSpecJ.withText("Archives")));

        Grid grid = _get(Grid.class);

        // this just count from my local database
        GridKt.expectRows(grid, 30);
    }

    @Test
    public void clickNewProjectShouldNavigateToNewProjectView() {
        _get(Button.class, spec -> spec.withText("New Project")).click();

        KaribuUtils.assertActiveViewClass(NewProjectView.class);
    }

}
