package io.skymind.pathmind.webapp.ui.plugins;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

@SpringComponent
@UIScope
@Tag("localstorage-helper")
@JsModule("./src/plugins/localstorage-helper.js")
public class LocalstorageHelper extends Component implements HasComponents {

    public LocalstorageHelper() {
    }

    public JsonObject getObject(String itemKey) {
        getElement().executeJs("return this.getItemAsObject('"+itemKey+"')")
                .then(JsonObject.class, result -> {
                    System.out.println(result);
                });
        return Json.createObject();
    }

    public void setItem(String itemKey, String itemValue) {
        getElement().callJsFunction("setItem", itemKey, itemValue);
    }

    public void setItemInObject(String itemKey, String objectFieldKey, String objectFieldValue) {
        getElement().callJsFunction("setItemInObject", itemKey, objectFieldKey, objectFieldValue);
    }

    public void setArrayItemInObject(String itemKey, String objectFieldKey, List<String> objectFieldValue) {
        JsonArray objectFieldValueArray = Json.createArray();
        for (int i = 0; i < objectFieldValue.size(); i++) {
            objectFieldValueArray.set(i, objectFieldValue.get(i));
        }
        getElement().callJsFunction("setItemInObject", itemKey, objectFieldKey, objectFieldValueArray);
    }

    public void setArrayItemInObjectOfObject(String itemKey, String objectFieldKey, String objectFieldInObjectKey, List<String> objectFieldInObjectValue) {
        JsonArray objectFieldValueArray = Json.createArray();
        for (int i = 0; i < objectFieldInObjectValue.size(); i++) {
            objectFieldValueArray.set(i, objectFieldInObjectValue.get(i));
        }
        getElement().callJsFunction("setItemInObjectOfObject", itemKey, objectFieldKey, objectFieldInObjectKey, objectFieldValueArray);
    }

}
