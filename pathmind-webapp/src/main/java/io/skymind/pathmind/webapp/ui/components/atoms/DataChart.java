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
    
	public interface Model extends TemplateModel {
		void setType(String type);
		void setShowtooltip(Boolean showTooltip);
		void setHAxisTitle(String hAxisTitle);
		void setVAxisTitle(String vAxisTitle);
		void setCurveLines(Boolean curveLines);
        void setSeriesType(String seriesType);
        void setSeries(JsonObject series);
        void setStacked(Boolean stacked);
        void setViewWindow(JsonObject viewWindow);
		void setCols(JsonArray cols);
		void setRows(JsonArray rows);
	}
}
