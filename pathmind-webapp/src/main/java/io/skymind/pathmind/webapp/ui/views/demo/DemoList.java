package io.skymind.pathmind.webapp.ui.views.demo;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.templatemodel.TemplateModel;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifest;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag("demo-list")
@JsModule("./src/components/organisms/demo-list.js")
public class DemoList extends PolymerTemplate<DemoList.Model> {

    private final SegmentIntegrator segmentIntegrator;
    private final DemoProjectService demoProjectService;
    private final List<ExperimentManifest> manifests;
    private Boolean createdDemoProject = false;

    public DemoList(DemoProjectService demoProjectService, ExperimentManifestRepository repo, SegmentIntegrator segmentIntegrator) {
        this.demoProjectService = demoProjectService;
        this.manifests = repo.getAll();
        this.segmentIntegrator = segmentIntegrator;
        setData();
        setUpButtonClickedHandler();
    }

    private void setUpButtonClickedHandler() {
        getElement().addPropertyChangeListener("name", event -> {
            if (!createdDemoProject) {
                createdDemoProject = true;
                try {
                    ExperimentManifest targetDemo;
                    String name = getModel().getName();
                    if (name != null) {
                        targetDemo = manifests.stream().filter(manifest -> manifest.getName().equals(name)).findFirst().orElse(null);
                        segmentIntegrator.createProjectFromExample(name);
                        if (targetDemo != null) {
                            Experiment experiment = demoProjectService.createExperiment(targetDemo, SecurityUtils.getUserId());
                            QueryParameters queryParam = QueryParameters.simple(Map.of("productTour",targetDemo.getName().replaceAll(" ", "").toLowerCase()));
                            getUI().ifPresent(ui -> ui.navigate(Routes.NEW_EXPERIMENT+"/"+experiment.getId(), queryParam));
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to handle project creation from demo chosen", e);
                    NotificationUtils.showError("Something went wrong. Please try again or contact Pathmind support.");
                }
            }
        });
    }

    public void setData() {
        JsonArray demoDataList = Json.createArray();
        for (ExperimentManifest manifest : manifests) {
            int index = demoDataList.length();
            JsonObject demoData = Json.createObject();
            demoData.put("name", manifest.getName());
            demoData.put("modelUrl", manifest.getModelUrl().toString());
            demoData.put("rewardFunction", manifest.getRewardFunction());
            demoData.put("imageUrl", manifest.getImageUrl().toString());
            demoData.put("description", manifest.getDescription());
            demoData.put("result", manifest.getResult());
            
            demoDataList.set(index, demoData);
        }
        getElement().callJsFunction("setData", demoDataList);
    }

    public void setIsVertical(Boolean isVertical) {
        getModel().setIsVertical(isVertical);
    }

    public interface Model extends TemplateModel {

        String getName();
        void setName(String name);

        void setIsVertical(Boolean isVertical);

    }

}
