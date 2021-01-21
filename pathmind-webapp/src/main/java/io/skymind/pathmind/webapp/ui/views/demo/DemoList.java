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
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;

@Tag("demo-list")
@JsModule("./src/components/organisms/demo-list.js")
public class DemoList extends PolymerTemplate<DemoList.Model> {

    private DemoProjectService demoProjectService;
    private List<ExperimentManifest> manifests;
    private Command onChooseDemoHandler = () -> {};
    private Boolean createdDemoProject = false;

    public DemoList(DemoProjectService demoProjectService, ExperimentManifestRepository repo) {
        this.demoProjectService = demoProjectService;
        this.manifests = repo.getAll();
        setData();
    }

    @EventHandler
    private void chooseDemoHandler(@EventData("event.model.item.name") String demoName) {
        if (!createdDemoProject) {
            createdDemoProject = true;
            try {
                ExperimentManifest targetDemo;
                onChooseDemoHandler.execute();
                if (demoName != null) {
                    targetDemo = manifests.stream().filter(manifest -> manifest.getName().equals(demoName)).findFirst().orElse(null);
                    if (targetDemo != null) {
                        Experiment experiment = demoProjectService.createExperiment(targetDemo, SecurityUtils.getUserId());
                        getUI().ifPresent(ui -> ui.navigate(NewExperimentView.class, experiment.getId()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
