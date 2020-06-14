package io.skymind.pathmind;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import io.skymind.pathmind.webapp.AbstractKaribuIntegrationTest;
import io.skymind.pathmind.webapp.ui.views.project.NewProjectView;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@WithUserDetails("steph@followsteph.com")
@Transactional
public class NewProjectViewTest extends AbstractKaribuIntegrationTest {

    @Before
    public void setUp() throws Exception {
        UI.getCurrent().navigate(NewProjectView.class);
    }

    @Test
    public void clickingCreateProjectWithoutGivingProjectNameShowsValidationError() {
        // first checking that there is no validation error
        assertNull(_get(TextField.class, spec -> spec.withCaption("Give your project a name")).getErrorMessage());

        _get(Button.class, spec -> spec.withText("Create Project")).click();

        assertEquals("Project must have a name", _get(TextField.class, spec -> spec.withCaption("Give your project a name")).getErrorMessage());

    }

    @Test
    public void name() throws IOException, InterruptedException {
        String projectName = UUID.randomUUID().toString();
        _get(TextField.class, spec -> spec.withCaption("Give your project a name")).setValue(projectName);

        _get(Button.class, spec -> spec.withText("Create Project")).click();


        // TODO below doesn't fire upload listeners in our PathmindModelUploader
        // most likely because we have quite much interaction with client side in the componennts

        // ClassPathResource res = new ClassPathResource("FAST_CoffeeShop_Database_5Observations_4Actions.zip");
        // byte[] bytes = IOUtils.resourceToByteArray("FAST_CoffeeShop_Database_5Observations_4Actions.zip", getClass().getClassLoader());
        // UploadKt._upload(_get(PathmindModelUploader.class), "FAST_CoffeeShop_Database_5Observations_4Actions.zip", bytes);


    }
}
