package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.NO_TOP_MARGIN_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.SECTION_SUBTITLE_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles.TRUNCATED_LABEL;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.binders.ModelBinders;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.PathmindTextArea;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.constants.CssMindPathStyles;

public class ModelDetailsWizardPanel extends VerticalLayout
{
	private VerticalLayout formPanel = new VerticalLayout();

	private Div sectionTitleWrapper;
	private Span projectNameLabel;
	private PathmindTextArea notesFieldTextArea;

	private Button nextStepButton = new Button("Next",  new Icon(VaadinIcon.CHEVRON_RIGHT));

	private Button draftButton = new Button("Save Draft", new Icon(VaadinIcon.FILE));

	public ModelDetailsWizardPanel(Binder<Model> binder, boolean isResumeUpload)
	{
		setupFields();
		setupForm();
		setupNotesFieldTextArea();
		nextStepButton.setIconAfterText(true);
		nextStepButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		sectionTitleWrapper = new Div();
		Span projectText = new Span("Project: ");
		projectText.addClassName(SECTION_TITLE_LABEL);
		projectNameLabel = LabelFactory.createLabel("", SECTION_TITLE_LABEL_REGULAR_FONT_WEIGHT, SECTION_SUBTITLE_LABEL);
		sectionTitleWrapper.add(projectText, projectNameLabel);
		sectionTitleWrapper.addClassName(TRUNCATED_LABEL);

		Icon checkmarkIcon = new Icon(VaadinIcon.COMMENTS.CHECK_CIRCLE);
		checkmarkIcon.setColor("var(--pm-friendly-color)");

		draftButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		HorizontalLayout modelDetailsLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
				LabelFactory.createLabel("Model Details", NO_TOP_MARGIN_LABEL),
				draftButton);
		modelDetailsLine.getStyle().set("align-items", "center");

		List<Component> items = new ArrayList<>(
				Arrays.asList(
						sectionTitleWrapper,
						modelDetailsLine,
						GuiUtils.getFullWidthHr()
				)
		);
		if (!isResumeUpload) {
			HorizontalLayout successMessage = WrapperUtils.wrapWidthFullHorizontal(
					checkmarkIcon,
					WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
							LabelFactory.createLabel("Your model was successfully uploaded!", BOLD_LABEL))
			);
			items.add(successMessage);
		}
		items.addAll(
				Arrays.asList(
						formPanel,
						WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton)
				)
		);

		add(items.toArray(new Component[0]));

		bindFields(binder);

		setWidthFull();
		setClassName("view-section"); // adds the white 'panel' style with rounded corners
	}

	private void setupFields()
	{
		notesFieldTextArea = new PathmindTextArea();
		notesFieldTextArea.setPlaceholder("Add your notes here");
	}

	private void bindFields(Binder<Model> binder) {
		ModelBinders.bindNotesFieldTextArea(binder, notesFieldTextArea);
	}

	public String getModelNotes() {
		return notesFieldTextArea.getValue();
	}

	public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		nextStepButton.addClickListener(listener);
	}

	public void addSaveDraftClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		draftButton.addClickListener(listener);
	}

	public void setProjectName(String name) {
		projectNameLabel.setText(name);
	}

	private void setupNotesFieldTextArea() {
		notesFieldTextArea.setWidthFull();
		notesFieldTextArea.setHeight("200px");
	}

	private void setupForm() {
		formPanel.add(getNotesFieldPanel());
		formPanel.setPadding(false);
	}

	private Component getNotesFieldPanel() {
		VerticalLayout wrapper = WrapperUtils.wrapWidthFullVertical(
				LabelFactory.createLabel("Notes", CssMindPathStyles.BOLD_LABEL),
				LabelFactory.createLabel("Add any notes for yourself about the model you're uploading."),
				notesFieldTextArea);
		GuiUtils.removeMarginsPaddingAndSpacing(wrapper);
		return wrapper;
	}
}
