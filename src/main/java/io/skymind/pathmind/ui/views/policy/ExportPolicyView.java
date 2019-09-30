package io.skymind.pathmind.ui.views.policy;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.utils.NotificationUtils;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("frontend://styles/styles.css")
@Route(value = "exportPolicy", layout = MainLayout.class)
public class ExportPolicyView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	private Logger log = LogManager.getLogger(ExportPolicyView.class);

	@Autowired
	private PolicyDAO policyDAO;

	private ScreenTitlePanel screenTitlePanel;

	private TextField nameTextField;

	private Image optimizedPolicyImage;
	private Button exportButton;
	private Button cancelButton;

	private long policyId;
	private Policy policy;

	public ExportPolicyView()
	{
		super();
	}

	@Override
	protected Component getTitlePanel() {
		screenTitlePanel = new ScreenTitlePanel("EXPORT");
		return screenTitlePanel;
	}

	@Override
	protected Component getMainContent()
	{
		// TODO -> DH -> Add validations.
		nameTextField = new TextField();
		nameTextField.setLabel("Name");
		nameTextField.setWidthFull();

		// TODO -> CSS
		optimizedPolicyImage = new Image("/frontend/images/exportPolicyIcon.gif", "Export Policy");

		// TODO -> CSS
		exportButton = new Button("Export", click -> handleExportButtonClicked());
		exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		exportButton.setWidth("200px");

		cancelButton = new Button("Cancel", click -> handleCancelButtonClicked());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		return WrapperUtils.wrapFormCenterVertical(
				nameTextField,
				optimizedPolicyImage,
				exportButton,
				cancelButton);
	}

	private void handleExportButtonClicked() {
		NotificationUtils.showTodoNotification();
	}

	private void handleCancelButtonClicked() {
		UI.getCurrent().navigate(ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(policy));
	}

	@Override
	public void setParameter(BeforeEvent event, Long policyId)
	{
		this.policyId = policyId;
	}

	@Override
	protected void loadData() throws InvalidDataException {
		policy = policyDAO.getPolicy(policyId);
		if(policy == null)
			throw new InvalidDataException("Attempted to access Policy: " + policyId);
	}

	@Override
	protected void updateScreen(BeforeEnterEvent event) throws InvalidDataException
	{
		// TODO -> DH -> Do we need to do anything else here?
	}
}
