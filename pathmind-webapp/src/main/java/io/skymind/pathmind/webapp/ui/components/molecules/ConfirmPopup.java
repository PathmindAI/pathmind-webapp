package io.skymind.pathmind.webapp.ui.components.molecules;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("confirm-popup")
@JsModule("./src/components/molecules/confirm-popup.js")
public class ConfirmPopup extends PolymerTemplate<ConfirmPopup.Model> {
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
        getModel().setHeaderText(text);
    }

    public void setMessage(String message) {
        getModel().setMessage(message);
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
        getModel().setOpened(true);
    }

    @EventHandler
    public void closePopup() {
        getElement().removeFromParent();
        getModel().setOpened(false);
    }

    @EventHandler
    private void onConfirm() {
        this.confirmHandler.execute();
        closePopup();
    }

    @EventHandler
    private void onCancel() {
        this.cancelHandler.execute();
        closePopup();
    }

    public void setConfirmButtonText(String confirmText) {
        getModel().setConfirmText(confirmText);
    }

    public void setCancelButtonText(String cancelText) {
        getModel().setCancelText(cancelText);
    }

    public void setConfirmButtonThemes(String confirmThemes) {
        getModel().setConfirmButtonThemes(confirmThemes);
    }

    public interface Model extends TemplateModel {
        void setHeaderText(String headerText);

        void setMessage(String message);

        void setCancelText(String cancelText);

        void setConfirmText(String confirmText);

        void setConfirmButtonThemes(String confirmButtonThemes);

        void setOpened(Boolean opened);
    }
}