package io.skymind.pathmind.webapp.ui.views.experiment.components;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.SparklineChart;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

import java.util.Map;

public class MetricChartPanel extends VerticalLayout {

    private SparklineChart chart = new SparklineChart();
    private Span chartLabel = LabelFactory.createLabel("", BOLD_LABEL);
    private Paragraph description = new Paragraph("This chart is a screenshot at the time of opening. It does not update automatically.");

    public MetricChartPanel(Map<Integer, Double> sparklineData, RewardVariable rewardVariable, Boolean reachedGoal) {
        setupChart(sparklineData, rewardVariable);
        HorizontalLayout titleWrapper = WrapperUtils.wrapWidthFullHorizontal(chartLabel);
        titleWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        String goalCondition = rewardVariable.getGoalConditionTypeEnum() != null ? rewardVariable.getGoalConditionTypeEnum().toString() : null;
        Double goalValue = rewardVariable.getGoalValue();
        if (goalCondition != null && goalValue != null) {
            TagLabel goalLabel = new TagLabel("Goal: "+goalCondition+goalValue, true, "small");
            if (reachedGoal) {
                goalLabel.addClassName("success-text");
            } else {
                goalLabel.addClassName("failure-text");
            }
            titleWrapper.add(goalLabel);
        }

        add(titleWrapper, description, chart);
        setPadding(false);
        setSpacing(false);
        addClassName("metric-chart-panel");
    }

    public void setupChart(Map<Integer, Double> sparklineData, RewardVariable rewardVariable) {
        chartLabel.setText(rewardVariable.getName());
        chart.setSparkLine(sparklineData, rewardVariable, true);
    }
}