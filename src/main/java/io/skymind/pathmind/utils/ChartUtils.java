package io.skymind.pathmind.utils;

import com.vaadin.flow.component.charts.model.PlotOptionsSeries;

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
}
