package io.skymind.pathmind.ui.views.model.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabsVariant;

import io.skymind.pathmind.ui.components.TabPanel;

public class UploadModelStatusWizardPanel extends VerticalLayout {

	private static final String UPLOAD_MODEL = "Upload Model";
	private static final String MODEL_DETAILS = "Model Details";

	private TabPanel tabPanel = new TabPanel(
			UPLOAD_MODEL,
			MODEL_DETAILS);

	public UploadModelStatusWizardPanel()
	{
		tabPanel.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS, TabsVariant.LUMO_CENTERED);
		tabPanel.setEnabled(false);

		add(tabPanel);

		setWidthFull();
		setMargin(false);
	}

	public void setUploadModel() {
		tabPanel.setTab(UPLOAD_MODEL);
	}

	public void setModelDetails() {
		tabPanel.setTab(MODEL_DETAILS);
	}
}
