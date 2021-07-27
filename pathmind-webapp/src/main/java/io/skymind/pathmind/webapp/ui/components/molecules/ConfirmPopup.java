package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag("confirm-popup")
@JsModule("./src/components/molecules/confirm-popup.ts")
public class ConfirmPopup extends LitTemplate {
    private Command confirmHandler = () -> {
    };
    private Command cancelHandler = () -> {
    };

    public ConfirmPopup() {
        attachToDOM();
    }

    public ConfirmPopup(String header, String message) {
        this();
        setHeader(header);
        setMessage(message);
    }

    public ConfirmPopup(String header, String message, String confirmText, Command confirmHandler) {
        this();
        setHeader(header);
        setMessage(message);
        setConfirmButton(confirmText, confirmHandler);
    }

    public ConfirmPopup(String header, String message, String confirmText, Command confirmHandler, String cancelText, Command cancelHandler) {
        this(header, message, confirmText, confirmHandler);
        setCancelButton(cancelText, cancelHandler);
    }

    public void setHeader(String text) {
        getElement().setProperty("headerText", text);
    }

    public void setMessage(String message) {
        getElement().setProperty("message", message);
    }

    public void setMessage(Component component) {
        getElement().appendChild(component.getElement());
    }

    public void setConfirmButton(String confirmText, Command confirmHandler) {
        this.confirmHandler = confirmHandler;
        setConfirmButtonText(confirmText);
    }

    public void setConfirmButton(String confirmText, Command confirmHandler, String themes) {
        setConfirmButton(confirmText, confirmHandler);
        setConfirmButtonThemes(themes);
    }

    public void setCancelButton(String cancelText, Command cancelHandler) {
        this.cancelHandler = cancelHandler;
        setCancelButtonText(cancelText);
    }

    public void attachToDOM() {
        UI ui = UI.getCurrent();
        if (getElement().getNode().getParent() == null) {
            ui.add(this);
        }
    }

    public void open() {
        attachToDOM();
        getElement().setProperty("opened", true);
    }

    @ClientCallable
    public void closePopup() {
        getElement().removeFromParent();
        getElement().setProperty("opened", false);
    }

    @ClientCallable
    private void onConfirm() {
        this.confirmHandler.execute();
        closePopup();
    }

    @ClientCallable
    private void onCancel() {
        this.cancelHandler.execute();
        closePopup();
    }

    public void setConfirmButtonText(String confirmText) {
        getElement().setProperty("confirmText", confirmText);
    }

    public void setCancelButtonText(String cancelText) {
        getElement().setProperty("cancelText", cancelText);
    }

    public void setConfirmButtonThemes(String confirmThemes) {
        getElement().setProperty("confirmButtonThemes", confirmThemes);
    }

}