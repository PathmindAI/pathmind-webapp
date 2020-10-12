package io.skymind.pathmind.webapp.ui.views.model.components;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.Command;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.bus.events.view.RewardVariableSelectedViewBusEvent;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.utils.GuiUtils;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.chart.AllMetricsChartPanel;

import java.util.*;

@CssImport(value = "./styles/components/reward-variables-table.css")
public class RewardVariablesTable extends VerticalLayout {

    private List<RewardVariable> rewardVariablesInComparison = new ArrayList<>();
	private List<RowField> rewardVariableNameFields = new ArrayList<>();
    private VerticalLayout container;
    private AllMetricsChartPanel allMetricsChartPanel;
    private Command goalFieldValueChangeHandler;
    private Boolean actAsMultiSelect = false;

    public RewardVariablesTable() {
        this(() -> {});
    }

    public RewardVariablesTable(Command goalFieldValueChangeHandler) {
        this.goalFieldValueChangeHandler = goalFieldValueChangeHandler;
        setPadding(false);
        setSpacing(false);
        container = WrapperUtils.wrapVerticalWithNoPaddingOrSpacing();
        container.setClassName("reward-variables-table");

        add(container);
    }

    public void setCodeEditorMode() {
        setClassName("with-container-border");
    }
    
    public void setCompactMode() {
        container.addClassName("compact");
    }

    public void setSelectMode() {
        actAsMultiSelect = true;
    }
    
    /**
     * By the default the table is readonly, as there is only a single case it's editable
     */
    public void makeEditable() {
        rewardVariableNameFields.forEach(f -> f.setEditable(true));
    }

    public void setRewardVariables(List<RewardVariable> rewardVariables) {
        container.removeAll();
        rewardVariableNameFields.clear();
        rewardVariablesInComparison.clear();
        HorizontalLayout headerRow = WrapperUtils.wrapWidthFullHorizontal(new Span("Variable Name"), new Span("Goal"));

        headerRow.addClassName("header-row");
        GuiUtils.removeMarginsPaddingAndSpacing(headerRow);

        container.add(headerRow);
        
        Collections.sort(rewardVariables, Comparator.comparing(RewardVariable::getArrayIndex));
        rewardVariables.forEach(rv -> {
            container.add(createRow(rv));
            rewardVariablesInComparison.add(rv);
        });
    }

    public void resetRewardVariablesInComparison(List<RewardVariable> rewardVariables) {
        rewardVariablesInComparison.clear();
        rewardVariables.forEach(rv -> rewardVariablesInComparison.add(rv));
        rewardVariableNameFields.forEach(field -> field.getRewardVariableSpan().getElement().setAttribute("chosen", true));
    }

    public void setAllMetricsChartPanel(AllMetricsChartPanel allMetricsChartPanel) {
        this.allMetricsChartPanel = allMetricsChartPanel;
    }
    
    public boolean canSaveChanges() {
        return rewardVariableNameFields.stream().allMatch(f -> f.isValid());
    }

    private RowField createRow(RewardVariable rv) {
        Command rewardVariableClickHandler = () -> {
            Optional<RewardVariable> thisRVinComparison = rewardVariablesInComparison.stream().filter(rvInComparison -> rv.equals(rvInComparison)).findAny();
            thisRVinComparison.ifPresentOrElse(
                    thisRV -> {
                        // EXAMPLE -> EventBus.post(new RewardVariableSelectedViewBusEvent(rewardVariable));
                        rewardVariablesInComparison.set(rv.getArrayIndex(), null);
                        allMetricsChartPanel.updateSelectedRewardVariables(rewardVariablesInComparison);
                    },
                    () -> {
                        rewardVariablesInComparison.set(rv.getArrayIndex(), rv);
                        allMetricsChartPanel.updateSelectedRewardVariables(rewardVariablesInComparison);
                    });
        };
        RowField rewardVariableNameField = new RowField(rv, goalFieldValueChangeHandler, rewardVariableClickHandler, actAsMultiSelect);
        rewardVariableNameFields.add(rewardVariableNameField);
        return rewardVariableNameField;
    }

    public List<RewardVariable> getRewardVariablesInComparison() {
        return rewardVariablesInComparison;
    }

    private static class RowField extends HorizontalLayout {
        private Span rewardVariableNameSpan;
        
        private NumberField goalField;
        private Select<GoalConditionType> conditionType;
        private HorizontalLayout goalFieldsWrapper;
        private Span goalSpan;
        
        private Binder<RewardVariable> binder;
        private Binding<RewardVariable, Double> goalValueBinding;
        private String goalOperatorSelectThemeNames = "goals small align-center";
        private Command goalFieldValueChangeHandler;

        private RowField(RewardVariable rv, Command goalFieldValueChangeHandler, Command clickHandler, Boolean actAsMultiSelect) {
            this.goalFieldValueChangeHandler = goalFieldValueChangeHandler;
            setAlignItems(Alignment.BASELINE);
            rewardVariableNameSpan = LabelFactory.createLabel(rv.getName(), "reward-variable-name");
            if (actAsMultiSelect) {
                String clickedAttribute = "chosen";
                rewardVariableNameSpan.getElement().setAttribute(clickedAttribute, true);

                rewardVariableNameSpan.addClickListener(event -> {
                    Element spanElement = event.getSource().getElement();
                    if (spanElement.hasAttribute(clickedAttribute)) {
                        spanElement.removeAttribute(clickedAttribute);
                    } else {
                        spanElement.setAttribute(clickedAttribute, true);
                    }
                    clickHandler.execute();
                });
            }
            
            conditionType = new Select<>();
            conditionType.setItems(GoalConditionType.LESS_THAN_OR_EQUAL, GoalConditionType.GREATER_THAN_OR_EQUAL);
            conditionType.setItemLabelGenerator(type -> type != null ? type.toString() : "None");
            // The item label generator did not add "None" to the dropdown
            // It only shows if the empty item is selected 
            conditionType.setEmptySelectionAllowed(true);
            // This is for the item label on the dropdown
            conditionType.setEmptySelectionCaption("None");
            conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            conditionType.addValueChangeListener(event -> setGoalFieldVisibility());
            
            goalField = new NumberField();
            goalField.addClassName("goal-field");
            goalField.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_RIGHT);
            goalField.addValueChangeListener(event -> goalFieldValueChangeHandler.execute());
            
            String goalDisplayText = rv.getGoalConditionType() == null ? "—" : String.format(rv.getGoalConditionTypeEnum().toString()+rv.getGoalValue());
            goalSpan = LabelFactory.createLabel(goalDisplayText, "goal-display-span");
            
            goalFieldsWrapper = WrapperUtils.wrapWidthFullHorizontal(conditionType, goalField, goalSpan);
            goalFieldsWrapper.addClassName("goal-fields-wrapper");
            goalFieldsWrapper.setVisible(false);
            GuiUtils.removeMarginsPaddingAndSpacing(goalFieldsWrapper);
            
            add(rewardVariableNameSpan, goalFieldsWrapper, goalSpan);
            setWidthFull();
            GuiUtils.removeMarginsPaddingAndSpacing(this);
            initBinder(rv);
            setGoalFieldVisibility();
        }
        
        private void initBinder(RewardVariable rv) {
            binder = new Binder<>();
            binder.bind(conditionType, RewardVariable::getGoalConditionTypeEnum, RewardVariable::setGoalConditionTypeEnum);
            goalValueBinding = binder.forField(goalField).asRequired("Enter a goal value").bind(RewardVariable::getGoalValue, RewardVariable::setGoalValue);
            binder.setBean(rv);
        }
        
        private void setGoalFieldVisibility() {
            if (conditionType.getValue() != null) {
                conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames+" not-none");
            } else {
                conditionType.getElement().setAttribute("theme", goalOperatorSelectThemeNames);
            }
            goalValueBinding.setAsRequiredEnabled(conditionType.getValue() != null);
            goalField.setVisible(conditionType.getValue() != null);
            goalField.setEnabled(conditionType.getValue() != null);
            goalFieldValueChangeHandler.execute();
        }
        
        public boolean isValid() {
            return binder.validate().isOk();
        }
        
        public void setEditable(boolean editable) {
            goalFieldsWrapper.setVisible(editable);
            goalSpan.setVisible(!editable);
        }

        public Span getRewardVariableSpan() {
            return rewardVariableNameSpan;
        }
        
    }
}
