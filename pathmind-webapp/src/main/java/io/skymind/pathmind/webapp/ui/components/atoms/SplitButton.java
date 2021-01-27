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
    private List<String> buttonTextList;
    private Command mainButtonClickListener = () -> {};
    private String splitButtonTheme = "split-button";

    public SplitButton(List<Button> buttons) {
        this.buttons = buttons.stream().collect(Collectors.toMap(Button::getText, button -> button));
        buttonTextList = new ArrayList<String>(this.buttons.keySet());
        setupDropdown();
        setMainButton(buttonTextList.get(0));
        mainButton.addClickListener(event -> mainButtonClickListener.execute());
        mainButton.addThemeName(splitButtonTheme);
        mainButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        add(mainButton, dropdown);
        addClassName(splitButtonTheme);
        setSpacing(false);
    }

    public void setMainButton(String buttonText) {
        Button targetButton = buttons.get(buttonText);
        mainButton.setText(buttonText);
        setMainButtonClickListener(targetButton);
    }

    private void setMainButtonClickListener(Button targetButton) {
        mainButtonClickListener = () -> {
            targetButton.click();
        };
    }

    private void setupDropdown() {
        dropdown = new Select<>();
        dropdown.setItems(buttonTextList);
        dropdown.addValueChangeListener(event -> setMainButton(""+event.getValue()));
        dropdown.getElement().setAttribute("theme", splitButtonTheme);
        dropdown.setValue(buttonTextList.get(0));
    }

    public void addThemeName(String additionalThemeName) {
        addClassName(additionalThemeName);
        dropdown.getElement().setAttribute("theme", splitButtonTheme + " " + additionalThemeName);
        mainButton.addThemeName(additionalThemeName);
    }

}
