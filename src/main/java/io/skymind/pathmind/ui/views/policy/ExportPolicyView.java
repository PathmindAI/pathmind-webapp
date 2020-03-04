package io.skymind.pathmind.ui.views.policy;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.services.PolicyFileService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.db.dao.UserDAO;
import io.skymind.pathmind.exception.InvalidDataException;
import io.skymind.pathmind.security.Routes;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;

import static io.skymind.pathmind.utils.StringUtils.toCamelCase;

@CssImport("./styles/styles.css")
@Route(value = Routes.EXPORT_POLICY_URL, layout = MainLayout.class)
public class ExportPolicyView extends PathMindDefaultView implements HasUrlParameter<Long>
{
	@Autowired
	private PolicyDAO policyDAO;
	@Autowired
	private PolicyFileService policyFileService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private SegmentIntegrator segmentIntegrator;

	private ScreenTitlePanel screenTitlePanel;

	private TextField nameTextField;

	private Button exportButton;
	private Anchor exportLink;
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
		final String policyFileName = PolicyUtils.generatePolicyFileName(policy);

		exportButton = new Button("Export");
		exportButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		exportButton.setWidth("200px");
		exportButton.addClickListener(evt -> {
			policyDAO.updateExportedDate(policyId);
			segmentIntegrator.policyExported();
		});

		exportLink = new Anchor();
		exportLink.add(exportButton);
		exportLink.getElement().setAttribute("href", getResourceStream(policyFileName));
		exportLink.getElement().setAttribute("download", true);

		cancelButton = new Button("Cancel", click -> handleCancelButtonClicked());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		nameTextField = new TextField();
		nameTextField.setLabel("Name");
		nameTextField.setValue(policyFileName);
		nameTextField.setWidthFull();
		nameTextField.addValueChangeListener(change ->
			exportLink.setHref(getResourceStream(StringUtils.isEmpty(nameTextField.getValue()) ? policyFileName : nameTextField.getValue())));

		return WrapperUtils.wrapFormCenterVertical(
				nameTextField,
				new Image("/frontend/images/exportPolicyIcon.gif", "Export Policy"),
				exportLink,
				cancelButton);
	}
	
	private StreamResource getResourceStream(String filename) {
		return new StreamResource(filename,
				() -> new ByteArrayInputStream(policyFileService.getPolicyFile(policyId)));
	}

	private void handleCancelButtonClicked() {
		UI.getCurrent().navigate(ExperimentView.class, policy.getExperiment().getId());
	}

	@Override
	public void setParameter(BeforeEvent event, Long policyId) {
		this.policyId = policyId;
	}

	@Override
	protected boolean isAccessAllowedForUser() {
		return userDAO.isUserAllowedAccessToPolicy(policyId);
	}

	@Override
	protected void initLoadData() {
		policy = policyDAO.getPolicy(policyId);
		if(policy == null)
			throw new InvalidDataException("Attempted to access Policy: " + policyId);
	}
}
