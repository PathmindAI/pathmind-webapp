package io.skymind.pathmind;

import com.github.mvysny.kaributesting.v10.BasicUtilsKt;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextArea;
import io.skymind.pathmind.webapp.AbstractKaribuIntegrationTest;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertNone;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;


@WithUserDetails("steph@followsteph.com")
@Transactional
public class ProjectViewTest extends AbstractKaribuIntegrationTest {

    @Before
    public void setUp() throws Exception {
        UI.getCurrent().navigate(ProjectView.class, 3L);
    }

    @Test
    public void testUnsavedNotesIndicator() {
        TextArea textArea = _get(TextArea.class);

        // Initially Unsaved Notes! should be invisible
        _assertNone(Span.class, spec -> spec.withText("Unsaved Notes!"));
        _get(Icon.class);

        // After modifying value Unsaved Notes! should be visible
        textArea.setValue(UUID.randomUUID().toString());
        _get(Span.class, spec -> spec.withText("Unsaved Notes!"));


        // After blur "Unsaved Notes!" should be invisible again
        BasicUtilsKt._blur(textArea);
        _assertNone(Span.class, spec -> spec.withText("Unsaved Notes!"));
    }
}
