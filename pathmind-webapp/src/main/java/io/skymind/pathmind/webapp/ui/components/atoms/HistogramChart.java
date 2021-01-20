package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.List;

@Tag("histogram-chart")
@JsModule("./src/components/atoms/histogram-chart.js")
public class HistogramChart extends PolymerTemplate<HistogramChart.Model> implements HasStyle {
    public HistogramChart() {
        super();
    }

    public void setupChart(
            String hAxisTitle,
            String vAxisTitle,
            List<String> colors,
            JsonObject viewWindow
    ) {
        getModel().setHaxistitle(hAxisTitle);
        getModel().setVaxistitle(vAxisTitle);
        getModel().setColors(colors);

        // JsonObject and JsonArray are not allowed types for TemplateModel methods
        // So we have to set it through calling the JS function
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

    public interface Model extends TemplateModel {

        void setHaxistitle(String hAxisTitle);

        void setVaxistitle(String vAxisTitle);

        void setColors(List<String> colors);

    }
}
