package io.skymind.pathmind.webapp.ui.plugins;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
@Tag("localstorage-helper")
@JsModule("./src/plugins/localstorage-helper.js")
public class LocalstorageHelper extends Component implements HasComponents {
    public LocalstorageHelper() {

    }
    public void setItem(String itemKey, String itemValue) {
        getElement().callJsFunction("setItem", itemKey, itemValue);
    }
    public void setItemInObject(String itemKey, String objectFieldKey, String objectFieldValue) {
        getElement().callJsFunction("setItemInObject", itemKey, objectFieldKey, objectFieldValue);
    }
}
