package io.skymind.pathmind.webapp.ui.components;

import java.util.ArrayList;
import java.util.OptionalDouble;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Accessibility;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Marker;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SparklineChart extends VerticalLayout{
    
    private Chart chart = new Chart(ChartType.AREA);
    
    private int WIDTH = 100;
    private int HEIGHT = 48;
    

    public SparklineChart() {
        setPadding(false);
        setSpacing(false);
        setupChart();
        setWidth(WIDTH + "px");
        setHeight(HEIGHT + "px");
        add(chart);
    }
    private void setupChart() {
        chart.getConfiguration().setAccessibility(new Accessibility(false));
        chart.getConfiguration().getLegend().setEnabled(false);
        chart.getConfiguration().getTitle().setText("");
        chart.getStyle().set("backgroundColor", null);
        chart.getStyle().set("borderWidth", "0");
//        chart.getStyle().set("margin", "0");
        chart.getStyle().set("overflow", "none");
        
        chart.getConfiguration().getxAxis().getLabels().setEnabled(false);
        chart.getConfiguration().getxAxis().getTitle().setText(null);
        chart.getConfiguration().getxAxis().setStartOnTick(false);
        chart.getConfiguration().getxAxis().setEndOnTick(false);
        chart.getConfiguration().getxAxis().setTickPositions(new Number[0]);
        chart.getConfiguration().getyAxis().getLabels().setEnabled(false);
        chart.getConfiguration().getyAxis().getTitle().setText(null);
        chart.getConfiguration().getyAxis().setStartOnTick(false);
        chart.getConfiguration().getyAxis().setEndOnTick(false);
        chart.getConfiguration().getyAxis().setTickPositions(new Number[0]);
        chart.setHeight(WIDTH + "px");
        chart.setHeight(HEIGHT + "px");
        Tooltip tooltip = new Tooltip();
        tooltip.setEnabled(true);
        tooltip.setHideDelay(0);
        tooltip.setShared(true);
        tooltip.setFormatter("return this.y");
        chart.getConfiguration().setTooltip(tooltip);
    }

    public void setSparkLine(double[] sparklineData, int index) {
        ArrayList<Number> data = new ArrayList<>();
        for (int i = 0; i < sparklineData.length; i++) {
            data.add(sparklineData[i]);
        }
        OptionalDouble min = data.stream().mapToDouble(Number::doubleValue).min();
        OptionalDouble max = data.stream().mapToDouble(Number::doubleValue).max();
        OptionalDouble avg = data.stream().mapToDouble(Number::doubleValue).average();
        
        min.ifPresent(val -> chart.getConfiguration().getyAxis().setMin(val));
        max.ifPresent(val -> chart.getConfiguration().getyAxis().setMax(val));
        
        
        ListSeries series = new ListSeries(data);
        PlotOptionsSeries plotOptions = new PlotOptionsSeries();
        plotOptions.setMarker(new Marker(false));
        plotOptions.setColorIndex(index);

        avg.ifPresent(val -> {
            PlotLine target = new PlotLine(val);
            target.setClassName("target");
            chart.getConfiguration().getyAxis().addPlotLine(target);
        });
        
        series.setPlotOptions(plotOptions);
        chart.getConfiguration().addSeries(series);
        chart.drawChart(true);
    }
    
}
