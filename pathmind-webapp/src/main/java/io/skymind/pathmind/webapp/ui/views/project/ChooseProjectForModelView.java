package io.skymind.pathmind.webapp.ui.views.project;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.shared.security.Routes;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.exception.InvalidDataException;
import io.skymind.pathmind.webapp.ui.layouts.MainLayout;
import io.skymind.pathmind.webapp.ui.views.PathMindDefaultView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = Routes.CHOOSE_PROJECT_FOR_MODEL, layout = MainLayout.class)
public class ChooseProjectForModelView extends PathMindDefaultView implements HasUrlParameter<String> {

    private final ChooseProjectForModelViewContent chooseProjectForModelViewContent;

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    public ChooseProjectForModelView(ChooseProjectForModelViewContent chooseProjectForModelViewContent) {
        this.chooseProjectForModelViewContent = chooseProjectForModelViewContent;
    }

    protected Component getMainContent() {
        return chooseProjectForModelViewContent;
    }

    @Override
    protected Component getTitlePanel() {
        return null;
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        String[] segments = parameter.split("/");
        try {
            Long modelId = Long.valueOf(segments[0]);
            Model model = modelDAO.getModelIfAllowed(modelId, SecurityUtils.getUserId()).get();
            this.chooseProjectForModelViewContent.setModel(model);
        } catch (Exception e) {
            throw new InvalidDataException("You don't have permission to access this model");
        }
    }

}
