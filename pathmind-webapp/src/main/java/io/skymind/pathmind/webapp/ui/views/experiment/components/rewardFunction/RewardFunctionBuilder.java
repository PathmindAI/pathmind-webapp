package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;

import org.vaadin.jchristophe.SortableConfig;
import org.vaadin.jchristophe.SortableLayout;

import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.SortableRowWrapper;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

/**
 * Created as it's own component so that we can easily swap in AceEditor later
 * with minimal code impact. I extended it so that binding code etc, would work
 * as expected and be consistent with other components.
 */
public class RewardFunctionBuilder extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;
    private List<String> rewardFunctionErrors = new ArrayList<>();
    private List<RewardVariable> rewardVariables;
    private List<RewardFunctionRow> rewardFunctionRows = new ArrayList<>();
    private NewExperimentView newExperimentView;
    private Span rewardEditorErrorLabel;
    private List<JuicyAceEditor> rewardFunctionJuicyAceEditors = new ArrayList<>();
    private Binder<Experiment> binder;
    private SortableLayout sortableLayout;
    private VerticalLayout rowsWrapper;
    private Button newRVrowButton;
    private Button newBoxButton;

    public RewardFunctionBuilder(NewExperimentView newExperimentView) {
        super();
        this.newExperimentView = newExperimentView;

        setupEditorErrorLabel();

        setPadding(false);
        setSpacing(false);

        newRVrowButton = new Button("New Reward Variable Row", new Icon(VaadinIcon.PLUS), click -> createNewRVrow());
        newRVrowButton.setIconAfterText(false);
        newRVrowButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        newBoxButton = new Button("New Code Editor Row", new Icon(VaadinIcon.PLUS), click -> createNewBoxRow());
        newBoxButton.setIconAfterText(false);
        newBoxButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        rowsWrapper = new VerticalLayout();
        rowsWrapper.setSpacing(false);
        rowsWrapper.setPadding(false);

        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setAnimation(300);

        sortableLayout = new SortableLayout(rowsWrapper, sortableConfig);
        sortableLayout.setHandle("draggable-icon");

        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL),
                rewardEditorErrorLabel));
        add(sortableLayout);
        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
            newRVrowButton, newBoxButton
        ));

        addClassName("reward-fn-editor-panel");

    }

    private void createNewRVrow() {
        RewardFunctionRow row = new RewardFunctionRow(rewardVariables);
        rowsWrapper.add(new SortableRowWrapper(row));
    }

    private void createNewBoxRow() {
        rowsWrapper.add(new SortableRowWrapper(setupRewardFunctionJuicyAceEditor()));
    }

    private void setRewardVariables(List<RewardVariable> rewardVariables) {
        this.rewardVariables = experiment.getRewardVariables();
        Collections.sort(rewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
    }

    private HorizontalLayout setupRewardFunctionJuicyAceEditor() {
        JuicyAceEditor rewardFunctionJuicyAceEditor = new JuicyAceEditor();
        setupValueChangeListener(rewardFunctionJuicyAceEditor, newExperimentView);
        rewardFunctionJuicyAceEditor.setSizeFull();
        rewardFunctionJuicyAceEditor.setTheme(JuicyAceTheme.eclipse);
        rewardFunctionJuicyAceEditor.setMode(JuicyAceMode.java);
        rewardFunctionJuicyAceEditor.setWrapmode(false);
        NumberField weightField = new NumberField();
        weightField.setPlaceholder("Weight");
        weightField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        weightField.addValueChangeListener(event -> {});
        HorizontalLayout wrapper = WrapperUtils.wrapWidthFullHorizontal(rewardFunctionJuicyAceEditor, new Span("x"), weightField);
        wrapper.setSpacing(false);
        rewardFunctionJuicyAceEditors.add(rewardFunctionJuicyAceEditor);
        return wrapper;
    }

    private void setupEditorErrorLabel() {
        rewardEditorErrorLabel = LabelFactory.createLabel(
                "Max. " + Experiment.REWARD_FUNCTION_MAX_LENGTH + " characters. Extra characters will not be saved.", "reward-editor-error");
        rewardEditorErrorLabel.setVisible(false);
    }

    private void setupValueChangeListener(JuicyAceEditor editor, NewExperimentView newExperimentView) {
        editor.addValueChangeListener(changeEvent -> {
            rewardEditorErrorLabel.setVisible(changeEvent.getValue().length() > Experiment.REWARD_FUNCTION_MAX_LENGTH);
            if (!experiment.getRewardFunction().equals(changeEvent.getValue())) {
                // REFACTOR -> We're overwriting the binder's utility here. We should investigate why and adjust accordingly.
                experiment.setRewardFunction(changeEvent.getValue());
                // REFACTOR -> This whole listener should possibly be in it's own action class but for now we'll just put the NeedsSavingAction as it's
                // reused in multiple parts of the code.
                NeedsSavingAction.setNeedsSaving(newExperimentView);
            }
        });
    }

    public void setVariableNames(List<RewardVariable> rewardVariables) {
        rewardFunctionJuicyAceEditors.forEach(editor -> {
            editor.setAutoComplete(rewardVariables);
        });
    }

    public boolean isValidForTraining(JuicyAceEditor rewardFunctionJuicyAceEditor) {
        return rewardFunctionJuicyAceEditor.getOptionalValue().isPresent()
                && !rewardFunctionJuicyAceEditor.getValue().isEmpty()
                && rewardFunctionErrors.size() == 0;
                // && isRewardFunctionLessThanMaxLength();
    }

    public boolean isRewardFunctionLessThanMaxLength(JuicyAceEditor rewardFunctionJuicyAceEditor) {
        return rewardFunctionJuicyAceEditor.getValue().length() <= Experiment.REWARD_FUNCTION_MAX_LENGTH;
    }

    public boolean isRewardFunctionMoreThanMaxLength() {
        return false; //!isRewardFunctionLessThanMaxLength();
    }

    public void setExperiment(Experiment experiment) {
        setEnabled(!experiment.isArchived());
        this.experiment = experiment;
        // binder.setBean(experiment);
        setRewardVariables(experiment.getRewardVariables());
        setVariableNames(experiment.getRewardVariables());
        // TODO -> need to set reward function rows value
        createNewRVrow();
        createNewBoxRow();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
        // TODO -> set reward function rows value
        // experiment.setRewardFunction(rewardFunctionJuicyAceEditor.getValue());
    }
}
