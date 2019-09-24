package io.skymind.pathmind.ui.views.experiment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.db.dao.ModelDAO;
import io.skymind.pathmind.db.repositories.ExperimentRepository;
import io.skymind.pathmind.ui.components.ActionMenu;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.ExceptionWrapperUtils;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.components.panels.ExperimentListPanel;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value="experiments", layout = MainLayout.class)
public class ExperimentsView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private ExperimentRepository experimentRepository;
	@Autowired
	private ModelDAO modelDAO;

	private long modelId;

	private ExperimentListPanel experimentListPanel;

	public ExperimentsView()
	{
		super();
	}

	protected Component getMainContent()
	{
		experimentListPanel = new ExperimentListPanel();
		experimentListPanel.addSelectionListener(selectedExperiment ->
				UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(selectedExperiment)));

		return experimentListPanel;
	}

	@Override
	protected ActionMenu getActionMenu() {
		return new ActionMenu(
				new Button("Back to Models", click ->
						ExceptionWrapperUtils.handleButtonClicked(() -> {
							UI.getCurrent().navigate(ModelsView.class, modelDAO.getProjectIdForModel(modelId));
						})),
				new Button("New Experiment", click ->
						NotificationUtils.showTodoNotification()));
//						UI.getCurrent().getCurrent().navigate(NewProjectView.class)));
	}

	@Override
	protected Component getTitlePanel() {
		return new ScreenTitlePanel("EXPERIMENTS");
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) {
		// TODO -> Only handles the happy path, no error checking that the modelId is valid.
		experimentListPanel.update(experimentRepository.getExperimentsForModel(modelId));
	}

	@Override
	public void setParameter(BeforeEvent event, Long modelId)
	{
		this.modelId = modelId;
	}
}
