package io.skymind.pathmind.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;

import io.skymind.pathmind.data.Policy;
import io.skymind.pathmind.ui.views.experiment.chart.RewardScoreSeriesItem;

public class ChartUtils {
	private static String ACTIVE_SERIES = "pm-active-series";
	private static String PASSIVE_SERIES = "pm-passive-series";
	
	public static PlotOptionsSeries createActiveSeriesPlotOptions() {
		return createPlotOptionsWithStyle(ACTIVE_SERIES);
	}
	public static PlotOptionsSeries createPassiveSeriesPlotOptions() {
		return createPlotOptionsWithStyle(PASSIVE_SERIES);
	}
	
	private static PlotOptionsSeries createPlotOptionsWithStyle(String style) {
		PlotOptionsSeries plotOptions = new PlotOptionsSeries();
		plotOptions.setClassName(style);
		return plotOptions;
	}
	
	public static List<DataSeriesItem> getRewardScoreSeriesItems(Policy policy) {
        return policy.getScores().stream()
        	.filter(score -> !Double.isNaN(score.getMean()))
            .map(RewardScoreSeriesItem::new)
            .collect(Collectors.toList());
    }
}
