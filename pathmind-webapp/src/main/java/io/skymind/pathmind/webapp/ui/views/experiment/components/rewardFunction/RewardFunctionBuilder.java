package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.services.RewardValidationService;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.RewardTerm;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.shared.utils.ExperimentUtils;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.SortableRowWrapper;
import io.skymind.pathmind.webapp.ui.components.atoms.ToggleButton;
import io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.NewExperimentView;
import io.skymind.pathmind.webapp.ui.views.experiment.actions.newExperiment.NeedsSavingAction;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RewardFunctionBuilder extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;
    private List<RewardVariable> rewardVariables;

    private final NewExperimentView newExperimentView;

    private final RewardValidationService rewardValidationService;

    private final ToggleButton betaToggleButton;

    private final VerticalLayout rowsWrapper;
    private final VerticalLayout editorWrapper;
    private RewardFunctionEditorRow rewardFunctionEditorRow;

    private final Map<String, RewardTermRow> rewardTermsRows = new HashMap<>();

    private List<RewardTerm> initState;

    private boolean experimentIsRewardTermsOn = false;

    public RewardFunctionBuilder(NewExperimentView newExperimentView,
                                 RewardValidationService rewardValidationService,
                                 boolean userAccountIsRewardTermsOn) {
        super();
        this.newExperimentView = newExperimentView;
        this.rewardValidationService = rewardValidationService;

        setSpacing(false);
        setPadding(false);

        rowsWrapper = new VerticalLayout();
        rowsWrapper.addClassName("reward-terms-wrapper");
        rowsWrapper.setSpacing(false);
        rowsWrapper.setPadding(false);

        betaToggleButton = new ToggleButton("Reward Terms BETA", "Reward Function", this::toggleBetweenBetaAndLive);
        betaToggleButton.setVisible(userAccountIsRewardTermsOn);

        HorizontalLayout header = WrapperUtils.wrapWidthFullBetweenHorizontal(
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL),
                betaToggleButton);
        header.addClassName("reward-function-header");
        add(header);

        editorWrapper = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();

        add(editorWrapper);

        addClassName("reward-fn-editor-panel");
    }

    private void toggleBetweenBetaAndLive() {
        if (experimentIsRewardTermsOn) {
            // going to switch from beta to old UI, save reward terms
            setRewardTermsToExperiment();
            newExperimentView.getExperimentDAO().updateRewardFunctionFromTerms(experiment);
            setupOldUI();
        } else {
            // going to switch from old UI to beta, save reward function
            setRewardFunctionToExperiment();
            newExperimentView.getExperimentDAO().updateRewardFunction(experiment);
            setupBetaUI();
        }
        experimentIsRewardTermsOn = !experimentIsRewardTermsOn;
        experiment.setWithRewardTerms(experimentIsRewardTermsOn);
        newExperimentView.getExperimentDAO().updateWithRewardTerms(experiment);
        betaToggleButton.setToggleButtonState(experimentIsRewardTermsOn);
        newExperimentView.setButtonsEnablement();
    }

    private void setupBetaUI() {
        editorWrapper.removeAll();
        editorWrapper.removeClassName("reward-function-editor-wrapper");

        Button newRowButton = new Button("Reward Term", new Icon(VaadinIcon.PLUS), click -> createNewRow());
        newRowButton.setIconAfterText(false);
        newRowButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        Button newBoxButton = new Button("Custom", new Icon(VaadinIcon.PLUS), click -> createNewBoxRow());
        newBoxButton.setIconAfterText(false);
        newBoxButton.addThemeVariants(ButtonVariant.LUMO_SMALL);

        editorWrapper.add(rowsWrapper,
                WrapperUtils.wrapWidthFullCenterHorizontal(newRowButton, newBoxButton));

        setViewWithRewardTerms(experiment.getRewardTerms());
    }

    private void setupOldUI() {
        editorWrapper.removeAll();

        rewardFunctionEditorRow = new RewardFunctionEditorRow(rewardVariables, rewardValidationService, this::changeHandler, true);

        rewardFunctionEditorRow.setExperiment(experiment);

        editorWrapper.add(rewardFunctionEditorRow);

        editorWrapper.addClassName("reward-function-editor-wrapper");
    }

    private void createNewRow() {
        createNewRow(new RewardTerm());
    }

    private void createNewRow(RewardTerm rewardTerm) {
        RewardTerm clonedRewardTerm = rewardTerm.deepClone();
        RewardFunctionRow row = new RewardFunctionRow(rewardVariables, this::changeHandler);
        row.setValue(clonedRewardTerm);
        putRewardTermsRow(row);
    }

    private void createNewBoxRow() {
        createNewBoxRow(new RewardTerm());
    }

    private void createNewBoxRow(RewardTerm rewardTerm) {
        RewardFunctionEditorRow row = new RewardFunctionEditorRow(rewardVariables, rewardValidationService, this::changeHandler, false);
        RewardTerm clonedRewardTerm = rewardTerm.deepClone();
        row.setValue(clonedRewardTerm);
        putRewardTermsRow(row);
    }

    private void changeHandler() {
        setNeedsSaving();
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

    private void setViewWithRewardTerms(List<RewardTerm> rewardTerms) {

        initState = ListUtils.emptyIfNull(rewardTerms).stream().map(RewardTerm::deepClone).collect(Collectors.toUnmodifiableList());

        rowsWrapper.removeAll();

        HorizontalLayout headerRow = new HorizontalLayout(new Span("Metric"), new Span("Goal"), new Span("Weight"));
        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);
        rowsWrapper.add(headerRow);

        rewardTerms.sort(Comparator.comparing(RewardTerm::getIndex));

        for (RewardTerm term : rewardTerms) {
            if (StringUtils.isNotEmpty(term.getRewardSnippet())) {
                createNewBoxRow(term);
            } else {
                createNewRow(term);
            }
        }
    }

    private void setRewardVariables(List<RewardVariable> rewardVariables) {
        this.rewardVariables = ListUtils.emptyIfNull(experiment.getRewardVariables());
        rewardVariables.sort(Comparator.comparing(RewardVariable::getArrayIndex));
    }

    public void resize() {
        if (experimentIsRewardTermsOn) {
            allComponentsRows().forEach(RewardTermRow::resize);
        } else {
            rewardFunctionEditorRow.resize();
        }
    }

    public boolean isValidForTraining() {
        if (experimentIsRewardTermsOn) {
            boolean editorRowsHaveErrors = allComponentsRows().anyMatch(RewardTermRow::hasErrors);
            return !editorRowsHaveErrors && loadTermsFromComponent().size() > 0;
        }
        return !rewardFunctionEditorRow.getRewardFunctionValue().isEmpty()
                && rewardFunctionEditorRow.getRewardFunctionErrorsSize() == 0
                && rewardFunctionEditorRow.isRewardFunctionLessThanMaxLength();
    }

    private void setNeedsSaving() {
        if (experimentIsRewardTermsOn) {
            if (checkRewardTermsListEquals()) {
                newExperimentView.disableSaveNeeded();
            } else {
                NeedsSavingAction.setNeedsSaving(newExperimentView);
            }
        } else {
            NeedsSavingAction.setNeedsSaving(newExperimentView);
        }
    }

    private boolean checkRewardTermsListEquals() {
        List<RewardTerm> actual = loadTermsFromComponent();
        return CollectionUtils.isEqualCollection(actual, initState);
    }

    public void setExperiment(Experiment experiment) {
        setEnabled(!experiment.isArchived());
        this.experiment = experiment;
        experimentIsRewardTermsOn = experiment.isWithRewardTerms();
        betaToggleButton.setToggleButtonState(experimentIsRewardTermsOn);
        setRewardVariables(experiment.getRewardVariables());
        if (experimentIsRewardTermsOn) {
            setupBetaUI();
        } else {
            setupOldUI();
        }
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
        if (experimentIsRewardTermsOn) {
            setRewardTermsToExperiment();
            setViewWithRewardTerms(experiment.getRewardTerms());
        } else {
            setRewardFunctionToExperiment();
        }
    }

    private void setRewardTermsToExperiment() {
        List<RewardTerm> terms = loadTermsFromComponent();

        List<String> rewardFunctionSnippets = terms.stream()
                .map(this::generateSnippetForTerm)
                .collect(Collectors.toList());

        String generatedRewardFunction = ExperimentUtils.collectRewardTermsToSnippet(rewardFunctionSnippets);
        experiment.setRewardTerms(terms);
        experiment.setRewardFunctionFromTerms(generatedRewardFunction);
    }

    private void setRewardFunctionToExperiment() {
        String rewardFunction = rewardFunctionEditorRow.getRewardFunctionValue();
        experiment.setRewardFunction(rewardFunction);

        String generatedRewardFunction = ExperimentUtils.collectRewardTermsToSnippet(Collections.singletonList(rewardFunction));
        experiment.setRewardFunctionFromTerms(generatedRewardFunction);

    }

    private Stream<RewardTermRow> allComponentsRows() {
        return rowsWrapper.getChildren()
                .map(Component::getId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(rewardTermsRows::get);
    }

    private List<RewardTerm> loadTermsFromComponent() {
        List<RewardTerm> terms = new ArrayList<>();
        allComponentsRows()
                .map(termComponent -> termComponent.convertToValueIfValid(terms.size()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(terms::add);
        return terms;
    }

    private String generateSnippetForTerm(RewardTerm term) {
        String snippet = term.getRewardSnippet();
        if (StringUtils.isEmpty(snippet)) {
            snippet = ExperimentUtils.generateRewardFunction(
                    rewardVariables.get(term.getRewardVariableIndex()),
                    term.getGoalCondition()
            );
        }
        return snippet;
    }
}
