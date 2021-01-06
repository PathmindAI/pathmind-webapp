package io.skymind.pathmind.webapp.ui.views.experiment.components.chart;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.webapp.ui.views.experiment.components.ExperimentComponent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CompareMetricsChartPanel extends VerticalLayout implements ExperimentComponent {

    private CompareMetricsChart chart = new CompareMetricsChart();

    private Experiment experiment;

    public CompareMetricsChartPanel() {
        add(hintMessage(), chart);
        setPadding(false);
        setSpacing(false);
    }

    private Paragraph hintMessage() {
        Paragraph hintMessage = new Paragraph(VaadinIcon.INFO_CIRCLE_O.create());
        hintMessage.add(
                "Select any two metrics on the simulation metric names above for comparison."
        );
        hintMessage.addClassName("hint-label");
        return hintMessage;
    }

    public void redrawChart() {
        chart.redraw();
    }

    public Experiment getExperiment() {
        return experiment;
    }

    @Override
    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        updateChart();
    }

    public long getExperimentId() {
        return experiment.getId();
    }

    public void updateChart() {
        // Update chart data
        chart.setCompareMetricsChart(experiment.getSelectedRewardVariables(), experiment.getBestPolicy());
        redrawChart();
    }
}