package io.skymind.pathmind.ui.views.policy;

import java.io.ByteArrayInputStream;

import io.skymind.pathmind.data.utils.PolicyUtils;
import io.skymind.pathmind.services.PolicyFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import io.skymind.pathmind.ui.components.LabelFactory;
import io.skymind.pathmind.ui.components.ScreenTitlePanel;
import io.skymind.pathmind.ui.components.ViewSection;
import io.skymind.pathmind.ui.components.navigation.Breadcrumbs;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;
import io.skymind.pathmind.ui.layouts.MainLayout;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.ui.utils.WrapperUtils;
import io.skymind.pathmind.ui.views.PathMindDefaultView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;

import static io.skymind.pathmind.utils.StringUtils.removeInvalidChars;

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
		return null;
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

		Anchor learnMoreLink = new Anchor("https://help.pathmind.com/en/articles/3655157-9-validate-trained-policy", "Learn how to validate your policy");
		learnMoreLink.setTarget("_blank");

		cancelButton = new Button("Cancel", click -> handleCancelButtonClicked());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		nameTextField = new TextField();
		nameTextField.setLabel("File Name");
		nameTextField.setValue(policyFileName);
		nameTextField.setWidthFull();
		nameTextField.addValueChangeListener(change ->
			exportLink.setHref(getResourceStream(StringUtils.isEmpty(nameTextField.getValue()) ? policyFileName : nameTextField.getValue())));

		VerticalLayout wrapperContent = WrapperUtils.wrapFormCenterVertical(
						LabelFactory.createLabel("Export", CssMindPathStyles.SECTION_TITLE_LABEL),
						new Image("/frontend/images/exportPolicyIcon.gif", "Export Policy"),
						nameTextField,
						createInstructionsDiv(),
						learnMoreLink,
						exportLink);
		wrapperContent.setClassName("view-section");
		return WrapperUtils.wrapCenterVertical("100%", 
				WrapperUtils.wrapWidthFullCenterHorizontal(createBreadcrumbs()),
				wrapperContent,
				cancelButton);
	}
	
	private StreamResource getResourceStream(String filename) {
		return new StreamResource(removeInvalidChars(filename),
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
	
	private Div createInstructionsDiv() {
		Div div = new Div();
		div.setClassName("export-instructions");
		div.getElement().setProperty("innerHTML",
				"<ol>" +
					"<li>Download this file</li>" +
					"<li>Open your AnyLogic simulation</li>" +
					"<li>Select the Pathmind Helper</li>" +
					"<li>Check \"Use Policy\"</li>" +
					"<li>Choose the policy file where you saved it</li>" +
					"<li>Run your simulation</li>" +
				"</ol>");
		return div;
	}
	
	private Breadcrumbs createBreadcrumbs() {        
		return new Breadcrumbs(policy.getProject(), policy.getModel(), policy.getExperiment(), "Export");
	}
}
