package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;

/**
 * Created as it's own component so that we can easily swap in AceEditor later
 * with minimal code impact. I extended it so that binding code etc, would work
 * as expected and be consistent with other components.
 */
public class RewardFunctionEditor extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;

    private List<String> rewardFunctionErrors = new ArrayList<>();

    private RewardFunctionErrorPanel rewardFunctionErrorPanel;
    private Span rewardEditorErrorLabel;

    private JuicyAceEditor rewardFunctionJuicyAceEditor;

    private Binder<Experiment> binder;

    private String rewardFunction = "";

    public RewardFunctionEditor(NewExperimentView newExperimentView, RewardValidationService rewardValidationService) {
        super();

        rewardFunctionErrorPanel = new RewardFunctionErrorPanel();

        setupRewardFunctionJuicyAceEditor();
        setupValueChangeListener(newExperimentView, rewardValidationService);
        setupEditorErrorLabel();

        setPadding(false);
        setSpacing(false);

        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL),
                rewardEditorErrorLabel));
        add(rewardFunctionJuicyAceEditor);

        addClassName("reward-fn-editor-panel");

        setupBinder();
    }

    private void setupRewardFunctionJuicyAceEditor() {
        rewardFunctionJuicyAceEditor = new JuicyAceEditor();
        rewardFunctionJuicyAceEditor.setSizeFull();
        rewardFunctionJuicyAceEditor.setTheme(JuicyAceTheme.eclipse);
        rewardFunctionJuicyAceEditor.setMode(JuicyAceMode.java);
        rewardFunctionJuicyAceEditor.setWrapmode(false);
    }

    private void setupEditorErrorLabel() {
        rewardEditorErrorLabel = LabelFactory.createLabel(
                "Reward Function must not exceed " + Experiment.REWARD_FUNCTION_MAX_LENGTH + " characters", "reward-editor-error");
        rewardEditorErrorLabel.setVisible(false);
    }

    private void setupValueChangeListener(NewExperimentView newExperimentView, RewardValidationService rewardValidationService) {
        rewardFunctionJuicyAceEditor.addValueChangeListener(changeEvent -> {
            rewardEditorErrorLabel.setVisible(changeEvent.getValue().length() > Experiment.REWARD_FUNCTION_MAX_LENGTH);
            rewardFunctionErrors = rewardValidationService.validateRewardFunction(rewardFunctionJuicyAceEditor.getValue(), experiment.getRewardVariables());
            rewardFunctionErrorPanel.showErrors(rewardFunctionErrors);
            if (!rewardFunction.equals(changeEvent.getValue())) {
                rewardFunction = changeEvent.getValue();
                // REFACTOR -> This whole listener should possibly be in it's own action class but for now we'll just put the NeedsSavingAction as it's
                // reused in multiple parts of the code.
                NeedsSavingAction.setNeedsSaving(newExperimentView);
            }
        });
    }

    public RewardFunctionErrorPanel getRewardFunctionErrorPanel() {
        return rewardFunctionErrorPanel;
    }

    public void setVariableNames(List<RewardVariable> rewardVariables) {
        rewardFunctionJuicyAceEditor.setAutoComplete(rewardVariables);
    }

    public boolean isValidForTraining() {
        return rewardFunctionJuicyAceEditor.getOptionalValue().isPresent()
                && !rewardFunctionJuicyAceEditor.getValue().isEmpty()
                && rewardFunctionErrors.size() == 0
                && isRewardFunctionLessThanMaxLength();
    }

    public boolean isRewardFunctionLessThanMaxLength() {
        return rewardFunctionJuicyAceEditor.getValue().length() <= Experiment.REWARD_FUNCTION_MAX_LENGTH;
    }

    public boolean isRewardFunctionMoreThanMaxLength() {
        return !isRewardFunctionLessThanMaxLength();
    }

    private void setupBinder() {
        binder = new Binder<>(Experiment.class);
        // To allow saving when the reward function editor is empty,
        // the field is not set to forField(...).asRequired().bind(...)
        binder.forField(rewardFunctionJuicyAceEditor).bind(Experiment::getRewardFunction,
                Experiment::setRewardFunction);
    }

    public boolean validateBinder() {
        return FormUtils.isValidForm(binder, experiment);
    }

    public void setExperiment(Experiment experiment) {
        setEnabled(!experiment.isArchived());
        this.experiment = experiment;
        ExperimentUtils.generateDefaultRewardFunction(experiment);
        binder.setBean(experiment);
        setVariableNames(experiment.getRewardVariables());
        rewardFunctionJuicyAceEditor.setValue(rewardFunction);
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
        experiment.setRewardFunction(rewardFunctionJuicyAceEditor.getValue());
    }
}
