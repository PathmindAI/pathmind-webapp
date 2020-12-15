package io.skymind.pathmind.webapp.ui.components.rewardVariables;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.model.UploadModelView;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.NO_TOP_MARGIN_LABEL;
import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.SECTION_SUBTITLE_LABEL;

public class RewardVariablesPanel extends VerticalLayout {
    private HorizontalLayout formPanel = WrapperUtils.wrapWidthFullHorizontal();
    private RewardVariablesTable rewardVariablesTable;

    private Button nextStepButton;

    private Supplier<Optional<UI>> getUISupplier;

    public RewardVariablesPanel(Supplier<Optional<UI>> getUISupplier) {
        this.getUISupplier = getUISupplier;
        setupForm();
        nextStepButton = UploadModelView.createNextStepButton();

        HorizontalLayout rewardVariablesNameLine = WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Variable Names", NO_TOP_MARGIN_LABEL));
        rewardVariablesNameLine.getStyle().set("align-items", "center");

        add(rewardVariablesNameLine,
                GuiUtils.getFullWidthHr(),
                new Paragraph("You have created a function to gather reward variables in your simulation. Here is the list of reward variables we extracted from your simulation."),
                LabelFactory.createLabel("Reward Variable Names → Goals", SECTION_SUBTITLE_LABEL, BOLD_LABEL),
                new Paragraph("Set the goals for the training by choosing which metrics you want to minimize or maximize."),
                formPanel,
                WrapperUtils.wrapWidthFullCenterHorizontal(nextStepButton));

        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    public void addButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        nextStepButton.addClickListener(listener);
    }

    private void setupForm() {
        rewardVariablesTable = new RewardVariablesTable(
                getUISupplier,
                () -> nextStepButton.setEnabled(canSaveChanges()));
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
