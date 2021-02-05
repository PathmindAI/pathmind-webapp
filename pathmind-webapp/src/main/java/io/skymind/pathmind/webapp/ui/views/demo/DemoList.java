package io.skymind.pathmind.webapp.ui.views.demo;

import java.util.List;

import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.templatemodel.TemplateModel;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import io.skymind.pathmind.services.project.demo.DemoProjectService;
import io.skymind.pathmind.services.project.demo.ExperimentManifest;
import io.skymind.pathmind.services.project.demo.ExperimentManifestRepository;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag("demo-list")
@JsModule("./src/components/organisms/demo-list.js")
public class DemoList extends PolymerTemplate<DemoList.Model> {

    private final SegmentIntegrator segmentIntegrator;
    private final DemoProjectService demoProjectService;
    private final List<ExperimentManifest> manifests;
    private Command onChooseDemoHandler = () -> {};
    private Boolean createdDemoProject = false;

    public DemoList(DemoProjectService demoProjectService, ExperimentManifestRepository repo, SegmentIntegrator segmentIntegrator) {
        this.demoProjectService = demoProjectService;
        this.manifests = repo.getAll();
        this.segmentIntegrator = segmentIntegrator;
        setData();
    }

    @EventHandler
    private void buttonClickedHandler(@EventData("event.target.getAttribute('name')") String demoName) {
        if (!createdDemoProject) {
            createdDemoProject = true;
            try {
                ExperimentManifest targetDemo;
                onChooseDemoHandler.execute();
                if (demoName != null) {
                    targetDemo = manifests.stream().filter(manifest -> manifest.getName().equals(demoName)).findFirst().orElse(null);
                    segmentIntegrator.createProjectFromExample(demoName);
                    if (targetDemo != null) {
                        Experiment experiment = demoProjectService.createExperiment(targetDemo, SecurityUtils.getUserId());
                        getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experiment.getId()));
                    }
                }
            } catch (Exception e) {
                log.error("Failed to handle project creation from demo chosen", e);
                NotificationUtils.showError("Something went wrong. Please try again or contact Pathmind support.");
            }
        }
    }

    public void setOnChooseDemoHandler(Command handler) {
        onChooseDemoHandler = handler;
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

    public interface Model extends TemplateModel {
        void setHideImage(Boolean hideImage);
    }

}
