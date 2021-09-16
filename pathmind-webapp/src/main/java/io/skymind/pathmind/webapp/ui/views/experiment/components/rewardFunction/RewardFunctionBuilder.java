package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardTerm;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.SortableRowWrapper;
import io.skymind.pathmind.webapp.ui.components.juicy.JuicyAceEditor;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RewardFunctionBuilder extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;

    private final NewExperimentView newExperimentView;

    private RewardValidationService rewardValidationService;

    private final VerticalLayout rowsWrapper;

    private final Map<String, RewardTermRow> rewardTermsRows = new HashMap<>();

    private List<RewardTerm> terms = new ArrayList<>();

    public RewardFunctionBuilder(NewExperimentView newExperimentView, RewardValidationService rewardValidationService) {
        super();
        this.newExperimentView = newExperimentView;
        this.rewardValidationService = rewardValidationService;

        setSpacing(false);
        setPadding(false);

        Button newRowButton = new Button("Reward Term", new Icon(VaadinIcon.PLUS), click -> createNewRow());
        newRowButton.setIconAfterText(false);
        newRowButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        Button newBoxButton = new Button("Custom", new Icon(VaadinIcon.PLUS), click -> createNewBoxRow());
        newBoxButton.setIconAfterText(false);
        newBoxButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        rowsWrapper = new VerticalLayout();
        rowsWrapper.addClassName("reward-terms-wrapper");
        rowsWrapper.setSpacing(false);
        rowsWrapper.setPadding(false);

        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL)));

        add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                rowsWrapper,
                WrapperUtils.wrapWidthFullCenterHorizontal(
                        newRowButton, newBoxButton)));

        addClassName("reward-fn-editor-panel");
    }

    private void createNewRow() {
        createNewRow(null, null, null);
    }

    private void createNewRow(RewardVariable variable, GoalConditionType goalCondition, Double weight) {
        RewardFunctionRow row = new RewardFunctionRow(rewardVariables, () -> setNeedsSaving());
        putRewardTermsRow(row);

        if (variable != null) {
            row.setRewardVariable(variable);
            row.setGoalCondition(goalCondition);
            row.setWeight(weight);
        }
    }

    private void createNewBoxRow() {
        createNewBoxRow(null, null);
    }

    private void createNewBoxRow(String snippet, Double weight) {
        RewardFunctionEditorRow row = new RewardFunctionEditorRow(rewardVariables);
        row.addEditorValueChangeListener(changeEvent -> {
            List<String> rewardFunctionErrors = rewardValidationService.validateRewardFunction(changeEvent.getValue(), experiment.getRewardVariables());
            row.setErrors(rewardFunctionErrors);
            setNeedsSaving();
        });
        putRewardTermsRow(row);

        if (StringUtils.isNotEmpty(snippet)) {
            row.setSnippet(snippet);
            row.setWeight(weight);
        }

    }

    private void putRewardTermsRow(RewardTermRow row) {
        String id = UUID.randomUUID().toString();
        SortableRowWrapper sortableRowWrapper = new SortableRowWrapper(row.asComponent(), false);
        sortableRowWrapper.setRemoveRowCallback(() -> {
            rewardTermsRows.remove(id);
            setNeedsSaving();
        });
        sortableRowWrapper.setId(id);
        rowsWrapper.add(sortableRowWrapper);
        rewardTermsRows.put(id, row);
    }

    private void setRewardTerms(List<RewardTerm> rewardTerms) {

        rowsWrapper.removeAll();
        rewardTermsRows.clear();

        HorizontalLayout headerRow = new HorizontalLayout(new Span("Metric"), new Span("Goal"), new Span("Weight"));
        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);
        rowsWrapper.add(headerRow);

        rewardTerms.sort(Comparator.comparing(RewardTerm::getIndex));

        for (RewardTerm term : rewardTerms) {
            if (StringUtils.isNotEmpty(term.getRewardSnippet())) {
                createNewBoxRow(term.getRewardSnippet(), term.getWeight());
            } else {
                createNewRow(this.rewardVariables.get(term.getRewardVariableIndex()), term.getGoalConditionType(), term.getWeight());
            }
        }
    }

    private void setRewardVariables(List<RewardVariable> rewardVariables) {
        this.rewardVariables = ListUtils.emptyIfNull(experiment.getRewardVariables());
        rewardVariables.sort(Comparator.comparing(RewardVariable::getArrayIndex));
    }

    public boolean isValidForTraining() {
        setRewardTermsWithInputValues();
        return terms.size() > 0;
    }


    private void setRewardTermsWithInputValues() {
        terms = new ArrayList<>();

        rowsWrapper.getChildren()
                .map(Component::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(rewardTermsRows::get)
                .map(termComponent -> {
                    if (termComponent instanceof RewardFunctionEditorRow) {
                        RewardFunctionEditorRow row = (RewardFunctionEditorRow) termComponent;
                        if (StringUtils.isEmpty(row.getSnippet())) {
                            return null;
                        }
                        return new RewardTerm(terms.size(), row.getWeight(), row.getSnippet());
                    }
                    if (termComponent instanceof RewardFunctionRow) {
                        RewardFunctionRow row = (RewardFunctionRow) termComponent;
                        if (row.getRewardVariable() == null || row.getGoalCondition() == null) {
                            return null;
                        }
                        return new RewardTerm(terms.size(), row.getWeight(), row.getRewardVariable().getArrayIndex(), row.getGoalCondition());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(terms::add);
    }

    private void setNeedsSaving() {
        setRewardTermsWithInputValues();
        System.out.println("experiment.getRewardTerms(): "+terms);
        System.out.println("terms: "+terms);
        System.out.println("experiment.getRewardTerms().containsAll(terms): "+experiment.getRewardTerms().containsAll(terms));
        if (!experiment.getRewardTerms().equals(terms)) {
            experiment.setRewardTerms(terms);
        }
        NeedsSavingAction.setNeedsSaving(newExperimentView);
    }

    public boolean isRewardFunctionLessThanMaxLength(JuicyAceEditor rewardFunctionJuicyAceEditor) {
        return rewardFunctionJuicyAceEditor.getValue().length() <= Experiment.REWARD_FUNCTION_MAX_LENGTH;
    }

    public void setExperiment(Experiment experiment) {
        setEnabled(!experiment.isArchived());
        this.experiment = experiment;
        setRewardVariables(experiment.getRewardVariables());
        setRewardTerms(experiment.getRewardTerms());
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
        setRewardTermsWithInputValues();
        experiment.setRewardTerms(terms);
        setRewardTerms(experiment.getRewardTerms());
    }
}
