package io.skymind.pathmind.webapp.ui.components;

import java.util.Map;

import com.google.gson.Gson;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("spark-line")
@NpmPackage(value = "@fnando/sparkline", version = "0.3.10")
@JsModule("/src/experiment/spark-line.js")
public class SparkLine extends PolymerTemplate<TemplateModel> implements HasStyle {
    public SparkLine() {
        super();
    }
    
    public void setSparkLine(Map<Integer, Double> sparklineData, int index) {
        Gson gson = new Gson();
        getElement().callJsFunction("setSparkLine", gson.toJson(sparklineData), index);
    }
}