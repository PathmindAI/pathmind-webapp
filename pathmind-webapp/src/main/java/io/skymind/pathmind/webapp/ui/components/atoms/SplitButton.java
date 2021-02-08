package io.skymind.pathmind.webapp.ui.components.atoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.server.Command;

public class SplitButton extends HorizontalLayout {

    private final Button mainButton = new Button();
    private Select dropdown;
    private Map<String, Button> buttons;
    private ArrayList<String> buttonTextList;
    private Command mainButtonClickListener = () -> {};
    private String splitButtonTheme = "split-button";

    public SplitButton(List<Button> buttons) {
        this.buttons = buttons.stream().collect(Collectors.toMap(Button::getText, button -> button));
        buttonTextList = new ArrayList<String>(this.buttons.keySet());
        setMainButton(buttonTextList.get(0));
        setupDropdown();
        add(mainButton, dropdown);
        addClassName(splitButtonTheme);
        setSpacing(false);
    }

    public void setMainButton(String buttonText) {
        Button targetButton = getTargetButtonFromButtonText(buttonText);
        setMainButtonClickListener(targetButton);
        mainButton.setText(buttonText);
        mainButton.addThemeName(splitButtonTheme);
        mainButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        mainButton.addClickListener(event -> mainButtonClickListener.execute());
    }

    private void setMainButtonClickListener(Button targetButton) {
        mainButtonClickListener = () -> {
            if (targetButton != null) {
                targetButton.click();
            }
        };
    }

    public void enableMainButton(Boolean enable) {
        mainButton.setEnabled(enable);
    }

    private Button getTargetButtonFromButtonText(String buttonText) {
        return buttons.get(buttonText);
    }

    private void setupDropdown() {
        ArrayList<String> buttonTextListWithoutFirst = (ArrayList<String>) buttonTextList.clone();
        buttonTextListWithoutFirst.remove(0);
        dropdown = new Select<>();
        dropdown.setEmptySelectionAllowed(true);
        dropdown.setItems(buttonTextListWithoutFirst);
        dropdown.addValueChangeListener(event -> {
            Button targetButton = getTargetButtonFromButtonText(""+event.getValue());
            if (targetButton != null) {
                targetButton.click();
            }
            dropdown.setValue("");
        });
        dropdown.getElement().setAttribute("theme", splitButtonTheme + " align-center");
    }

    public void addThemeName(String additionalThemeName) {
        addClassName(additionalThemeName);
        dropdown.getElement().setAttribute("theme", splitButtonTheme + " align-center " + additionalThemeName);
        mainButton.addThemeName(additionalThemeName);
    }

}
