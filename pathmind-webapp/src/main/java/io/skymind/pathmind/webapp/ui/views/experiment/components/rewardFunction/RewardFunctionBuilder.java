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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardTerm;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.jchristophe.SortableConfig;
import org.vaadin.jchristophe.SortableLayout;


@Slf4j
public class RewardFunctionBuilder extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;

    private final NewExperimentView newExperimentView;

    private final List<RewardFunctionRow> rewardFunctionRows = new ArrayList<>();
    private final List<JuicyAceEditor> rewardFunctionJuicyAceEditors = new ArrayList<>();

    private Binder<Experiment> binder;

    private final VerticalLayout rowsWrapper;

    private final Map<String, Object> rewardTermsRows = new HashMap<>();

    private List<String> rewardFunctionErrors = new ArrayList<>();

    public RewardFunctionBuilder(NewExperimentView newExperimentView) {
        super();
        this.newExperimentView = newExperimentView;

        setSpacing(false);
        setPadding(false);

        Button newRowButton = new Button("New Reward Variable Row", new Icon(VaadinIcon.PLUS), click -> createNewRow());
        newRowButton.setIconAfterText(false);
        newRowButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        Button newBoxButton = new Button("New Code Editor Row", new Icon(VaadinIcon.PLUS), click -> createNewBoxRow());
        newBoxButton.setIconAfterText(false);
        newBoxButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        rowsWrapper = new VerticalLayout();
        rowsWrapper.setSpacing(false);
        rowsWrapper.setPadding(false);

        SortableConfig sortableConfig = new SortableConfig();
        sortableConfig.setAnimation(300);

        SortableLayout sortableLayout = new SortableLayout(rowsWrapper, sortableConfig);
        sortableLayout.setHandle("draggable-icon");

        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL)));

        add(WrapperUtils.wrapVerticalWithNoPaddingOrSpacing(
                sortableLayout,
                WrapperUtils.wrapWidthFullCenterHorizontal(
                        newRowButton, newBoxButton)));

        addClassName("reward-fn-editor-panel");
    }

    private void createNewRow() {
        createNewRow(null, null, null);
    }

    private void createNewRow(RewardVariable variable, GoalConditionType goalCondition, Double weight) {
        RewardFunctionRow row = new RewardFunctionRow(rewardVariables);
        int newIndexOfRow = rewardFunctionRows.size();
        rewardFunctionRows.add(newIndexOfRow, row);
        SortableRowWrapper sortableRowWrapper = new SortableRowWrapper(row);

        String id = UUID.randomUUID().toString();
        sortableRowWrapper.setId(id);
        rewardTermsRows.put(id, row);

        sortableRowWrapper.setRemoveRowCallback(() -> {
            rewardFunctionRows.remove(newIndexOfRow);
            rewardTermsRows.remove(id);
        });
        rowsWrapper.add(sortableRowWrapper);

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
        JuicyAceEditor rewardFunctionEditor = setupRewardFunctionJuicyAceEditor();
        int newIndexOfEditor = rewardFunctionJuicyAceEditors.size();
        rewardFunctionJuicyAceEditors.add(newIndexOfEditor, rewardFunctionEditor);
        NumberField weightField = new NumberField();
        weightField.setPlaceholder("Weight");
        weightField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        weightField.addValueChangeListener(event -> {
        });
        HorizontalLayout boxRowWrapper = WrapperUtils.wrapWidthFullHorizontal(
                rewardFunctionEditor, new Span("x"), weightField);
        boxRowWrapper.setSpacing(false);
        SortableRowWrapper sortableRowWrapper = new SortableRowWrapper(boxRowWrapper);

        String id = UUID.randomUUID().toString();
        sortableRowWrapper.setId(id);
        rewardTermsRows.put(id, rewardFunctionEditor);

        sortableRowWrapper.setRemoveRowCallback(() -> {
            rewardFunctionJuicyAceEditors.remove(newIndexOfEditor);
            rewardTermsRows.remove(id);
        });
        rowsWrapper.add(sortableRowWrapper);

        if (StringUtils.isNotEmpty(snippet)) {
            rewardFunctionEditor.setValue(snippet);
            weightField.setValue(weight);
        }

    }

    private void setRewardTerms(List<RewardTerm> rewardTerms) {

        rowsWrapper.removeAll();

        rewardFunctionRows.clear();
        rewardFunctionJuicyAceEditors.clear();
        rewardTermsRows.clear();

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

    private JuicyAceEditor setupRewardFunctionJuicyAceEditor() {
        JuicyAceEditor rewardFunctionJuicyAceEditor = new JuicyAceEditor();
        setupValueChangeListener(rewardFunctionJuicyAceEditor, newExperimentView);
        rewardFunctionJuicyAceEditor.setSizeFull();
        rewardFunctionJuicyAceEditor.setTheme(JuicyAceTheme.eclipse);
        rewardFunctionJuicyAceEditor.setMode(JuicyAceMode.java);
        rewardFunctionJuicyAceEditor.setWrapmode(false);
        rewardFunctionJuicyAceEditor.setAutoComplete(rewardVariables);
        return rewardFunctionJuicyAceEditor;
    }

    private void setupValueChangeListener(JuicyAceEditor editor, NewExperimentView newExperimentView) {
        editor.addValueChangeListener(changeEvent -> {
            if (!experiment.getRewardFunction().equals(changeEvent.getValue())) {
                // REFACTOR -> We're overwriting the binder's utility here. We should investigate why and adjust accordingly.
                experiment.setRewardFunction(changeEvent.getValue());
                // REFACTOR -> This whole listener should possibly be in it's own action class but for now we'll just put the NeedsSavingAction as it's
                // reused in multiple parts of the code.
                NeedsSavingAction.setNeedsSaving(newExperimentView);
            }
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

    public void setExperiment(Experiment experiment) {
        setEnabled(!experiment.isArchived());
        this.experiment = experiment;
//        binder.setBean(experiment);
        setRewardVariables(experiment.getRewardVariables());
        setRewardTerms(experiment.getRewardTerms());
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {

        List<RewardTerm> terms = new ArrayList<>();

        rowsWrapper.getChildren()// todo: if reorder elements - order is not updated in the container.
                .map(Component::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(rewardTermsRows::get)
                .map(termComponent -> {
                    if (termComponent instanceof JuicyAceEditor) {
                        String snippet = StringUtils.trimToEmpty(((JuicyAceEditor) termComponent).getValue());
                        if (StringUtils.isEmpty(snippet)) {
                            return null;
                        }
                        Double weight = 1d; // todo: make component returning value and weight
                        return new RewardTerm(terms.size(), weight, snippet);
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


        experiment.setRewardTerms(terms);

    }
}
