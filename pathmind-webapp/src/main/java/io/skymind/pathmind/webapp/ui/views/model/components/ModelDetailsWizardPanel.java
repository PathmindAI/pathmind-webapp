package io.skymind.pathmind.webapp.ui.views.model.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.data.Model;
import io.skymind.pathmind.webapp.ui.binders.ModelBinders;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;

public class ModelDetailsWizardPanel extends VerticalLayout {
    private VerticalLayout formPanel = new VerticalLayout();

    private TextArea notesFieldTextArea;

    private Button nextStepButton;

    public ModelDetailsWizardPanel(Binder<Model> binder) {
        setupFields();
        setupForm();
        setupNotesFieldTextArea();
        nextStepButton = UploadModelView.createNextStepButton();

        HorizontalLayout modelDetailsLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Model Details", NO_TOP_MARGIN_LABEL));
        modelDetailsLine.getStyle().set("align-items", "center");

        List<Component> items = new ArrayList<>(
                Arrays.asList(
                        modelDetailsLine,
                        GuiUtils.getFullWidthHr()
                )
        );
        items.addAll(
                Arrays.asList(
                        formPanel,
                        WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton)
                )
        );

        add(items.toArray(new Component[0]));

        bindFields(binder);

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private void setupFields() {
        notesFieldTextArea = new TextArea();
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

    private void setupNotesFieldTextArea() {
        notesFieldTextArea.setWidthFull();
        notesFieldTextArea.setHeight("200px");
    }

    private void setupForm() {
        formPanel.add(getNotesFieldPanel());
        formPanel.setPadding(false);
    }

    private Component getNotesFieldPanel() {
        VerticalLayout wrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacingAndWidthAuto(
                LabelFactory.createLabel("Notes", CssPathmindStyles.BOLD_LABEL),
                LabelFactory.createLabel("Add any notes for yourself about the model you're uploading."),
                notesFieldTextArea);
        return wrapper;
    }
}
