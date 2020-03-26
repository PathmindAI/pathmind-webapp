package io.skymind.pathmind.webapp.ui.views.testfeature;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.featureflag.Feature;
import io.skymind.pathmind.shared.featureflag.FeatureManager;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = Routes.TEST_FEATURE_URL, layout = MainLayout.class)
public class TestFeatureView extends PathMindDefaultView {

    private final FeatureManager featureManager;

    @Autowired
    public TestFeatureView(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @Override
    protected boolean isAccessAllowedForUser() {
        return featureManager.isEnabled(Feature.TEST_FEATURE);
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    protected Component getMainContent() {
        return new Text("Hey, I'm a test view!");
    }
}
