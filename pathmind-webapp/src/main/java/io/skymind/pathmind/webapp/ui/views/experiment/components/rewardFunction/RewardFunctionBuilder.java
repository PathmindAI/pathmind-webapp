package io.skymind.pathmind.webapp.ui.views.experiment.components.rewardFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        createNewRow(new RewardTerm(terms.size()));
    }

    private void createNewRow(RewardTerm rewardTerm) {
        RewardFunctionRow row = new RewardFunctionRow(rewardVariables, () -> changeHandler());
        RewardTerm clonedRewardTerm = rewardTerm.deepClone();
        row.setValue(clonedRewardTerm);
        putRewardTermsRow(row);
        terms.add(clonedRewardTerm);
    }

    private void createNewBoxRow() {
        createNewBoxRow(new RewardTerm(terms.size()));
    }

    private void createNewBoxRow(RewardTerm rewardTerm) {
        RewardFunctionEditorRow row = new RewardFunctionEditorRow(rewardVariables, rewardValidationService, () -> changeHandler());
        RewardTerm clonedRewardTerm = rewardTerm.deepClone();
        row.setValue(clonedRewardTerm);
        putRewardTermsRow(row);
        terms.add(clonedRewardTerm);
    }

    private void changeHandler() {
        setNeedsSaving();
    }

    private void putRewardTermsRow(RewardTermRow row) {
        String id = UUID.randomUUID().toString();
        SortableRowWrapper sortableRowWrapper = new SortableRowWrapper(row.asComponent(), false);
        sortableRowWrapper.setRemoveRowCallback(() -> {
            rewardTermsRows.remove(id);
            terms.remove(row.getValue());
            setNeedsSaving();
        });
        sortableRowWrapper.setId(id);
        rowsWrapper.add(sortableRowWrapper);
        rewardTermsRows.put(id, row);
    }

    private void setViewWithRewardTerms(List<RewardTerm> rewardTerms) {
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

    public boolean isValidForTraining() {
        return terms.size() > 0;
    }

    private void setNeedsSaving() {
        if (checkRewardTermsListEquals()) {
            newExperimentView.disableSaveNeeded();
        } else {
            NeedsSavingAction.setNeedsSaving(newExperimentView);
        }
    }

    private boolean checkRewardTermsListEquals() {
        if (experiment.getRewardTerms().size() != terms.size()) {
            return false;
        }
        return !experiment.getRewardTerms().stream()
                .filter(rt ->  !rt.equals(terms.get(rt.getIndex())))
                .findAny()
                .isPresent();
    }

    private boolean isRewardFunctionLessThanMaxLength(JuicyAceEditor rewardFunctionJuicyAceEditor) {
        return rewardFunctionJuicyAceEditor.getValue().length() <= Experiment.REWARD_FUNCTION_MAX_LENGTH;
    }

    public void setExperiment(Experiment experiment) {
        setEnabled(!experiment.isArchived());
        this.experiment = experiment;
        terms.clear();
        setRewardVariables(experiment.getRewardVariables());
        setViewWithRewardTerms(experiment.getRewardTerms());
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void updateExperiment() {
        experiment.setRewardTerms(terms);
    }
}
