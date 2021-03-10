package io.skymind.pathmind.webapp.ui.components.atoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.select.Select;

public class ActionDropdown extends Select {

    private Map<String, Button> buttons;
    private ArrayList<String> buttonTextList;
    private String actionDropdownTheme = "action-dropdown";

    public ActionDropdown(List<Button> buttons) {
        this.buttons = buttons.stream().collect(Collectors.toMap(Button::getText, button -> button));
        buttonTextList = new ArrayList<String>(this.buttons.keySet());
        setEmptySelectionAllowed(true);
        setItems(buttonTextList);
        addValueChangeListener(event -> {
            Button targetButton = getTargetButtonFromButtonText(""+event.getValue());
            if (targetButton != null) {
                targetButton.click();
            }
            setValue("");
        });
        getElement().setAttribute("theme", actionDropdownTheme + " align-center");
    }

    private Button getTargetButtonFromButtonText(String buttonText) {
        return buttons.get(buttonText);
    }
    
}
