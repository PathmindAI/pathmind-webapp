package io.skymind.pathmind.webapp.ui.components;

import elemental.json.Json;
import elemental.json.JsonArray;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("spark-line")
@JsModule("/src/experiment/spark-line.js")
public class SparkLine extends PolymerTemplate<TemplateModel> implements HasStyle {
    public SparkLine() {
        super();
    }
    
    public void setSparkLine(float[] sparklineData, int index) {
        getElement().callJsFunction("setSparkLine", convertToJsArray(sparklineData), index);
    }
    
    public JsonArray convertToJsArray(float[] sparklineData) {
		JsonArray json = Json.createArray();
        if (sparklineData != null) {
            for (int i = 0; i < sparklineData.length; i++) {
                json.set(i, sparklineData[i]);
            }
        }
        return json;
    }
}