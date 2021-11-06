package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import java.util.List;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

public class RewardVariablesPanel extends VerticalLayout {
    private HorizontalLayout formPanel = WrapperUtils.wrapWidthFullHorizontal();
    private RewardVariablesTable rewardVariablesTable;

    private Button nextStepButton = FormUtils.createNextStepButton();
    private Button saveButton;
    private Button pageButton;

    public RewardVariablesPanel(Boolean useSaveButton) {
        setupForm();

        createSaveButton();

        pageButton = useSaveButton ? saveButton : nextStepButton;

        add(new Paragraph("Set the goals for the training by choosing which metrics you want to minimize or maximize."),
            formPanel,
            WrapperUtils.wrapWidthFullCenterHorizontal(pageButton));

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    public void createSaveButton() {
        saveButton = new Button("Save");
        saveButton.setIconAfterText(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        pageButton.addClickListener(listener);
    }

    private void setupForm() {
        rewardVariablesTable = new RewardVariablesTable();
        formPanel.setPadding(false);
        formPanel.add(rewardVariablesTable);
    }

    public void setupRewardVariables(List<RewardVariable> rewardVariables) {
        rewardVariablesTable.setRewardVariables(rewardVariables);
        rewardVariablesTable.makeEditable();
    }

    public boolean canSaveChanges() {
        return rewardVariablesTable.canSaveChanges();
    }
}
