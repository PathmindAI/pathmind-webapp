package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import io.skymind.pathmind.shared.data.RewardVariable;
import io.skymind.pathmind.webapp.ui.components.LabelFactory;
import io.skymind.pathmind.webapp.ui.components.atoms.TagLabel;
import io.skymind.pathmind.webapp.ui.utils.WrapperUtils;
import io.skymind.pathmind.webapp.ui.views.experiment.components.SparklineChart;

import static io.skymind.pathmind.webapp.ui.constants.CssPathmindStyles.BOLD_LABEL;

import java.util.Map;

public class MetricChartPanel extends VerticalLayout {

    private HorizontalLayout titleWrapper;
    private TagLabel goalLabel;
    private SparklineChart chart = new SparklineChart();
    private Span chartLabel = LabelFactory.createLabel("", BOLD_LABEL);

    public MetricChartPanel() {
        titleWrapper = WrapperUtils.wrapWidthFullHorizontal(chartLabel);
        titleWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        goalLabel = new TagLabel("", true, "small");
        titleWrapper.add(goalLabel);
        add(titleWrapper, chart);
        setPadding(false);
        setSpacing(false);
        addClassName("metric-chart-panel");
    }

    public void setGoals(RewardVariable rewardVariable, Boolean reachedGoal) {
        String goalCondition = rewardVariable.getGoalConditionTypeEnum() != null ? rewardVariable.getGoalConditionTypeEnum().toString() : null;
        Double goalValue = rewardVariable.getGoalValue();
        if (goalCondition != null && goalValue != null) {
            goalLabel.setVisible(true);
            goalLabel.setText("Goal: "+goalCondition+goalValue);
            if (reachedGoal) {
                goalLabel.setClassName("success-text");
            } else {
                goalLabel.setClassName("failure-text");
            }
        } else {
            goalLabel.setVisible(false);
        }
    }

    public void setupChart(Map<Integer, Double> sparklineData, RewardVariable rewardVariable) {
        chartLabel.setText(rewardVariable.getName());
        chart.setSparkLine(sparklineData, rewardVariable, true);
    }
}