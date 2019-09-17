package io.skymind.pathmind.ui.views.run;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.constants.Algorithm;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.constants.RunType;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.utils.FakeDataUtils;
import io.skymind.pathmind.db.ExperimentRepository;
import io.skymind.pathmind.db.ProjectRepository;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.PushUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.run.components.PolicyPanel;
import io.skymind.pathmind.ui.views.run.components.RunStatusPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "discoveryRunConfirmation", layout = MainLayout.class)
public class DiscoveryRunConfirmationView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private static final String START = "Start";
	private static final String STOP = "Stop";

	private Logger log = LogManager.getLogger(DiscoveryRunConfirmationView.class);

	private long experimentId = -1;
	private Experiment experiment;

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ExperimentRepository experimentRepository;

	private ScreenTitlePanel screenTitlePanel;
	private PolicyPanel policyPanel;
	private RunStatusPanel runStatusPanel;

	private Button actionButton;

	private UI ui;

	public DiscoveryRunConfirmationView()
	{
		super();
		this.ui = UI.getCurrent();
	}

	@Override
	protected Component getTitlePanel() {
		screenTitlePanel = new ScreenTitlePanel("PROJECT");
		return screenTitlePanel;
	}

	@Override
	protected Component getMainContent()
	{
		actionButton = new Button(START, click -> handleActionButtonClicked());
		policyPanel = new PolicyPanel();
		runStatusPanel = new RunStatusPanel(RunType.TestRun);

		runStatusPanel.setVisible(false);

		return WrapperUtils.wrapWidthFullCenterVertical(
			actionButton,
			WrapperUtils.wrapFormCenterVertical(
				policyPanel,
				runStatusPanel
			)
		);
	}

	private void handleActionButtonClicked() {
		if(START.equals(actionButton.getText()))
			handleStartButtonClicked();
		else
			handleStopButtonClicked();
	}

	// TODO -> What is the proper behavior for this button.
	private void handleStopButtonClicked() {
		actionButton.setText(START);
//		policyPanel.setVisible(true);
//		runStatusPanel.setVisible(false);
	}

	private void handleStartButtonClicked()
	{
		actionButton.setText(STOP);
		policyPanel.setVisible(false);
		runStatusPanel.setVisible(true);

		// TODO -> Implement.
//		experiment.setStatusEnum(RunStatus.Running);
//		experiment.setAlgorithm(Algorithm.DQN);
//		experiment.setCompleted(RunStatus.Running);
//		experiment.startExperimentNow();

		generateFakeData();
	}

	private void generateFakeData() {
		// TODO -> Implement as a service wtih real data.
		new Thread(() -> {
			try {
				Random random = new Random();
				for(int x=0; x<30; x++) {
					Thread.sleep(300);
					PushUtils.push(this, () -> {
						// TODO -> Re-implement with new data model.
//							experiment.getScores().add(random.nextInt(FakeDataUtils.EXPERIMENT_SCORE_MAX));
							runStatusPanel.update() ;
					});
				}
				// Done.
				PushUtils.push(this, () -> {
					// TODO -> Re-implement with new data model.
//					experiment.setStatusEnum(RunStatus.Completed);
//					experiment.setCompleted(RunStatus.Completed);
					runStatusPanel.update();
				});
			} catch (Exception e) {
				// All fake.
			}
		}).start();
	}

	@Override
	public void setParameter(BeforeEvent event, Long experimentId) {
		this.experimentId = experimentId;
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		experiment = experimentRepository.getExperiment(experimentId);
		if(experiment == null)
			throw new InvalidDataException("Attempted to access Experiment: " + experimentId);
		runStatusPanel.setExperiment(experiment);

		Project project = projectRepository.getProjectForExperiment(experimentId);
		screenTitlePanel.setSubtitle(project.getName());
	}
}
