package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

@Tag("data-chart")
@JsModule("./src/components/atoms/data-chart.ts")
public class DataChart extends LitTemplate implements HasStyle {
    public DataChart() {
        super();
    }

    public void setupChart(
            String type,
            Boolean showTooltip,
            String hAxisTitle,
            String vAxisTitle,
            Boolean curveLines,
            String seriesType,
            JsonObject series,
            Boolean stacked,
            JsonObject viewWindow
    ) {
        setupChart(type, showTooltip, hAxisTitle, vAxisTitle, null, null, null, null, curveLines, seriesType, series, stacked, viewWindow);
    }

    public void setupChart(
            String type,
            Boolean showTooltip,
            String hAxisTitle,
            String vAxisTitle,
            String metric1AxisTitle,
            String metric2AxisTitle,
            String metric1Color,
            String metric2Color,
            Boolean curveLines,
            String seriesType,
            JsonObject series,
            Boolean stacked,
            JsonObject viewWindow
    ) {
        getElement().setProperty("type", type);
        getElement().setProperty("showtooltip", showTooltip);
        getElement().setProperty("haxistitle", hAxisTitle);
        getElement().setProperty("vaxistitle", vAxisTitle);
        getElement().setProperty("metric1axistitle", metric1AxisTitle);
        getElement().setProperty("metric2axistitle", metric2AxisTitle);
        getElement().setProperty("metric1color", metric1Color);
        getElement().setProperty("metric2color", metric2Color);
        getElement().setProperty("curvelines", curveLines);
        getElement().setProperty("seriestype", seriesType);
        getElement().setProperty("stacked", stacked);

        // JsonObject and JsonArray are not allowed types for TemplateModel methods
        // So we have to set it through calling the JS function
        getElement().callJsFunction("setSeries", series);
        getElement().callJsFunction("setViewWindow", viewWindow);
    }

    public void setData(JsonArray cols, JsonArray rows) {
        // JsonObject and JsonArray are not allowed types for TemplateModel methods
        // So we have to set it through calling the JS function
        getElement().callJsFunction("setData", cols, rows);
        redraw();
    }

    public void setChartEmpty() {
        getElement().callJsFunction("setChartEmpty");
        redraw();
    }

    public void redraw() {
        getElement().callJsFunction("redraw");
    }
}
