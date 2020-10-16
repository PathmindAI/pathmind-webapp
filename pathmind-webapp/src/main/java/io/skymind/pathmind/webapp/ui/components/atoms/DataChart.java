package io.skymind.pathmind.webapp.ui.components.atoms;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

@Tag("data-chart")
@JsModule("./src/components/atoms/data-chart.js")
public class DataChart extends PolymerTemplate<DataChart.Model> implements HasStyle {
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
        getModel().setType(type);
        getModel().setShowtooltip(showTooltip);
        getModel().setHaxistitle(hAxisTitle);
        getModel().setVaxistitle(vAxisTitle);
        getModel().setCurvelines(curveLines);
        getModel().setSeriestype(seriesType);
        getModel().setStacked(stacked);

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
    
	public interface Model extends TemplateModel {
		void setType(String type);
		void setShowtooltip(Boolean showTooltip);
		void setHaxistitle(String hAxisTitle);
		void setVaxistitle(String vAxisTitle);
		void setCurvelines(Boolean curveLines);
        void setSeriestype(String seriesType);
        void setStacked(Boolean stacked);
        void setDimlines(Boolean useDimlines);
	}
}
