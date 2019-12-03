package io.skymind.pathmind.ui.views.project.components.wizard;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabsVariant;
import io.skymind.pathmind.ui.components.TabPanel;
import io.skymind.pathmind.ui.constants.CssMindPathStyles;

public class NewProjectStatusWizardPanel extends VerticalLayout
{
	private static final String CREATE_A_NEW_PROJECT = "Create A New Project";
	private static final String PATHMIND_HELPER = "Pathmind Helper";
	private static final String UPLOAD_MODEL = "Upload Model";
	private static final String MODEL_DETAILS = "Model Details";

	private TabPanel tabPanel = new TabPanel(
			CREATE_A_NEW_PROJECT,
			PATHMIND_HELPER,
			UPLOAD_MODEL,
			MODEL_DETAILS);

	public NewProjectStatusWizardPanel()
	{
		tabPanel.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS, TabsVariant.LUMO_CENTERED);
		tabPanel.setEnabled(false);

		add(tabPanel);

		setWidthFull();
		setMargin(false);
		setPadding(false);
		setSpacing(false);
		setClassName(CssMindPathStyles.SECTION_STATUS_PANEL);
	}

	public void setCreateANewProject() {
		tabPanel.setTab(CREATE_A_NEW_PROJECT);
	}

	public void setPathmindHelper() {
		tabPanel.setTab(PATHMIND_HELPER);
	}

	public void setUploadModel() {
		tabPanel.setTab(UPLOAD_MODEL);
	}

	public void setModelDetails() {
		tabPanel.setTab(MODEL_DETAILS);
	}
}
