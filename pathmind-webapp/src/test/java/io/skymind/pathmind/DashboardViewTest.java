package io.skymind.pathmind;

import com.github.mvysny.kaributesting.v10.RouterLinkKt;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;
import io.skymind.pathmind.webapp.AbstractKaribuIntegrationTest;
import io.skymind.pathmind.webapp.ui.views.project.NewProjectView;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@WithUserDetails("steph@followsteph.com")
public class DashboardViewTest extends AbstractKaribuIntegrationTest {

    @Test
    public void checkDashboardBeginScreenElements() {
        // for comparison, this does same assertions than our BDD test io.skymind.pathmind.bddtests.page.DashboardPage.checkDashboardBeginScreenElements
        // Benefit here is that no need to hassle with xpath selectors. also some selectors could be a little differently but wanted to follow the BDD test

        assertEquals(_get(Span.class, spec -> spec.withClasses("light-text-label")).getText(), "Welcome to"); // or just _get(Span.class, spec -> spec.withText("Welcome to"));
        assertEquals("frontend/images/pathmind-logo.png", _get(Image.class, spec -> spec.withClasses("navbar-logo")).getSrc());
        assertEquals(_get(Span.class, spec -> spec.withClasses("section-title-label")).getText(), "Let's begin by opening the");
        assertEquals(_get(Anchor.class, spec -> spec.withClasses("button-link")).getText(), "Getting Started Guide");
        assertEquals(_get(Anchor.class, spec -> spec.withClasses("button-link")).getHref(), "https://help.pathmind.com/en/articles/4004788-getting-started");
        _get(Span.class, spec -> spec.withText("or skip ahead to "));
        _get(RouterLink.class, spec -> spec.withText("create your first project."));
    }

    @Test
    public void clikingCreateYourFirstProjectShouldNavigateToNewProjectView() {
        RouterLinkKt.click(_get(RouterLink.class, spec -> spec.withText("create your first project.") ));
        assertSame(NewProjectView.class, KaribuUtils.getActiveViewClass());
    }
}
