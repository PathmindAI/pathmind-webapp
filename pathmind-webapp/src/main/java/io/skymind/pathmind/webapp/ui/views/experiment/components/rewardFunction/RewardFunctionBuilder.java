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

public class RewardFunctionBuilder extends VerticalLayout implements ExperimentComponent {

    private Experiment experiment;
    private List<String> rewardFunctionErrors = new ArrayList<>();
    private List<RewardVariable> rewardVariables;
    private List<RewardFunctionRow> rewardFunctionRows = new ArrayList<>();
    private List<JuicyAceEditor> rewardFunctionJuicyAceEditors = new ArrayList<>();
    private NewExperimentView newExperimentView;
    private Binder<Experiment> binder;
    private SortableLayout sortableLayout;
    private VerticalLayout rowsWrapper;
    private Button newRVrowButton;
    private Button newBoxButton;

    public RewardFunctionBuilder(NewExperimentView newExperimentView) {
        super();
        this.newExperimentView = newExperimentView;

        setSpacing(false);
        setPadding(false);

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
                LabelFactory.createLabel("Reward Function", CssPathmindStyles.BOLD_LABEL)));
        add(sortableLayout);
        add(WrapperUtils.wrapWidthFullBetweenHorizontal(
            newRVrowButton, newBoxButton
        ));

        addClassName("reward-fn-editor-panel");
    }

    private void createNewRVrow() {
        RewardFunctionRow row = new RewardFunctionRow(rewardVariables);
        int newIndexOfRow = rewardFunctionRows.size();
        rewardFunctionRows.add(newIndexOfRow, row);
        SortableRowWrapper sortableRowWrapper = new SortableRowWrapper(row);
        sortableRowWrapper.setRemoveRowCallback(() -> {
            System.out.print("before: "+rewardFunctionRows);
            rewardFunctionRows.remove(newIndexOfRow);
            System.out.print("after: "+rewardFunctionRows);
        });
        rowsWrapper.add(sortableRowWrapper);
    }

    private void createNewBoxRow() {
        JuicyAceEditor rewardFunctionEditor = setupRewardFunctionJuicyAceEditor();
        int newIndexOfEditor = rewardFunctionJuicyAceEditors.size();
        rewardFunctionJuicyAceEditors.add(newIndexOfEditor, rewardFunctionEditor);
        NumberField weightField = new NumberField();
        weightField.setPlaceholder("Weight");
        weightField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
        weightField.addValueChangeListener(event -> {});
        HorizontalLayout boxRowWrapper = WrapperUtils.wrapWidthFullHorizontal(
                rewardFunctionEditor, new Span("x"), weightField);
        boxRowWrapper.setSpacing(false);
        SortableRowWrapper sortableRowWrapper = new SortableRowWrapper(boxRowWrapper);
        sortableRowWrapper.setRemoveRowCallback(() -> {
            System.out.print("before: "+rewardFunctionJuicyAceEditors);
            rewardFunctionJuicyAceEditors.remove(newIndexOfEditor);
            System.out.print("after: "+rewardFunctionJuicyAceEditors);
        });
        rowsWrapper.add(sortableRowWrapper);
    }

    private void createOrSetRows() {
        // TODO -> need to get reward function rows in DB,
        // compare with current view,
        // then create/remove/set reward function rows value,
        // right now row data are not saved into the DB
        createNewRVrow();
        createNewBoxRow();
    }

    private void setRewardVariables(List<RewardVariable> rewardVariables) {
        this.rewardVariables = experiment.getRewardVariables();
        Collections.sort(rewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
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
        // binder.setBean(experiment);
        setRewardVariables(experiment.getRewardVariables());
        createOrSetRows();
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
