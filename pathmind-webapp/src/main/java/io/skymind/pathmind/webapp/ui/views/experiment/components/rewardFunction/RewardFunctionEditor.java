package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.EventBus;
import io.skymind.pathmind.webapp.bus.events.view.ExperimentChangedViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.components.juicy.mode.JuicyAceMode;
import io.skymind.pathmind.webapp.ui.components.juicy.theme.JuicyAceTheme;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.FormUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction.subscribers.view.RewardFunctionEditorExperimentSwitchedViewSubscriber;
import org.apache.commons.lang3.StringUtils;

/**
 * Created as it's own component so that we can easily swap in AceEditor later
 * with minimal code impact. I extended it so that binding code etc, would work
 * as expected and be consistent with other components.
 */
public class RewardFunctionEditor extends VerticalLayout {

    private final int REWARD_FUNCTION_MAX_LENGTH = 65535;

    private Supplier<Optional<UI>> getUISupplier;

    private Experiment experiment;

    private List<RewardVariable> rewardVariables;
    private List<String> rewardFunctionErrors = new ArrayList<>();

    private RewardFunctionErrorPanel rewardFunctionErrorPanel;
    private Span rewardEditorErrorLabel;

    private JuicyAceEditor rewardFunctionJuicyAceEditor;

    private RewardValidationService rewardValidationService;

    private Binder<Experiment> binder;

    private String rewardFunction = "";

    public RewardFunctionEditor(Supplier<Optional<UI>> getUISupplier, Experiment experiment, List<RewardVariable> rewardVariables, RewardValidationService rewardValidationService) {
        super();
        this.getUISupplier = getUISupplier;
        this.rewardVariables = rewardVariables;
        this.rewardValidationService = rewardValidationService;

        rewardFunctionErrorPanel = new RewardFunctionErrorPanel();

        setupRewardFunctionJuicyAceEditor();
        setupValueChangeListener(rewardValidationService);
        setupEditorErrorLabel();

        setPadding(false);
        setSpacing(false);

        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL),
                rewardEditorErrorLabel));
        add(rewardFunctionJuicyAceEditor);

        if (experiment.isArchived()) {
            setEnabled(false);
        }

        addClassName("reward-fn-editor-panel");

        setupBinder();
        setExperiment(experiment);
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
                "Reward Function must not exceed " + REWARD_FUNCTION_MAX_LENGTH + " characters", "reward-editor-error");
        rewardEditorErrorLabel.setVisible(false);
    }

    private void setupValueChangeListener(RewardValidationService rewardValidationService) {
        rewardFunctionJuicyAceEditor.addValueChangeListener(changeEvent -> {
            rewardEditorErrorLabel.setVisible(changeEvent.getValue().length() > REWARD_FUNCTION_MAX_LENGTH);
            rewardFunctionErrors = rewardValidationService.validateRewardFunction(rewardFunctionJuicyAceEditor.getValue(), rewardVariables);
            rewardFunctionErrorPanel.showErrors(rewardFunctionErrors);
            if(!rewardFunction.equals(changeEvent.getValue())) {
                rewardFunction = changeEvent.getValue();
                EventBus.post(new ExperimentChangedViewBusEvent(experiment));
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
        return rewardFunctionJuicyAceEditor.getValue().length() <= REWARD_FUNCTION_MAX_LENGTH;
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
        this.experiment = experiment;
        rewardFunction = StringUtils.defaultIfEmpty(experiment.getRewardFunction(), generateRewardFunction());
        binder.setBean(experiment);
        rewardFunctionJuicyAceEditor.setValue(rewardFunction);
    }

    private String generateRewardFunction() {
        StringBuilder sb = new StringBuilder();
        if (experiment.isHasGoals()) {
            for (RewardVariable rv : rewardVariables) {
                GoalConditionType goal = rv.getGoalConditionTypeEnum();
                if (goal != null) {
                    sb.append(MessageFormat.format("reward {0}= after.{1} - before.{1};", goal.getMathOperation(), rv.getName()));
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    @Override
    protected void onDetach(DetachEvent event) {
        EventBus.unsubscribe(this);
    }

    @Override
    protected void onAttach(AttachEvent event) {
        EventBus.subscribe(this, getUISupplier,
                new RewardFunctionEditorExperimentSwitchedViewSubscriber(this));
    }
}
