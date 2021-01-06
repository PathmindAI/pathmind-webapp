package io.skymind.pathmind.webapp.ui.views.demo;

import java.util.List;

import io.skymind.pathmind.services.project.demo.ExperimentManifest;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;

// TODO: @Tag("sign-up-view")
// TODO: @CssImport(value = "./styles/views/sign-up-view.css", id = "sign-up-view-styles")
// TODO: @JsModule("./src/pages/account/sign-up-view.js")
// TODO: @Route(value = Routes.SIGN_UP_URL)
public class DemoView // TODO: extends PolymerTemplate<SignUpView.Model> implements PublicView
{

    private final ExperimentManifestRepository repo;

    public DemoView(ExperimentManifestRepository repo) {
        this.repo = repo;
    }

    public void render() {
        List<ExperimentManifest> manifests =  repo.getAll();
    }

}
