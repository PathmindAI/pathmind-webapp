package io.skymind.pathmind.webapp.ui.components.molecules;

import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("notes-field")
@JsModule("./src/components/molecules/notes-field.js")
public class NotesField extends PolymerTemplate<NotesField.Model> {

    private static final int MAX_NOTES_SIZE = 1000;

    private String notesText;
    private Consumer<String> saveConsumer;
    private Command onNotesChangeHandler = () -> {};

    public NotesField(String title, String notesText, Consumer<String> saveConsumer) {
        this(title, notesText, saveConsumer, false, true, false);
    }

    public NotesField(String title, String notesText, Consumer<String> saveConsumer, Boolean compact) {
        this(title, notesText, saveConsumer, compact, true, false);
    }

    public NotesField(String title, String notesText, Consumer<String> saveConsumer, Boolean compact, Boolean allowAutoSave, Boolean hideSaveButton) {
        this.saveConsumer = saveConsumer;
        setNotesText(notesText);
        getModel().setTitle(title);
        getModel().setMax(MAX_NOTES_SIZE);
        setCompact(compact);
        setAllowAutoSave(allowAutoSave);
        setHideSaveButton(hideSaveButton);
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
        getElement().callJsFunction("_notesChanged", notesText);
    }

    public String getNotesText() {
        return notesText;
    }

    public void setPlaceholder(String placeholder) {
        getModel().setPlaceholder(placeholder);
    }

    public void setReadonly(Boolean readonly) {
        getModel().setReadonly(readonly);
    }

    public void setAllowAutoSave(Boolean allowAutoSave) {
        getModel().setAllowautosave(allowAutoSave);
    }

    public void setHideSaveButton(Boolean hideSaveButton) {
        getModel().setHidesavebutton(hideSaveButton);
    }

    public void setCompact(Boolean compact) {
        getModel().setCompact(compact);
    }

    public void setSecondaryStyle(Boolean secondaryStyle) {
        getModel().setSecondaryStyle(secondaryStyle);
    }

    public void setOnNotesChangeHandler(Command onNotesChangeHandler) {
        this.onNotesChangeHandler = onNotesChangeHandler;
    }

    @EventHandler
    private void onNotesChange(@EventData("event.target.value") String updatedNotesText) {
        if (canSave(updatedNotesText)) {
            notesText = updatedNotesText;
            onNotesChangeHandler.execute();
        }
    }

    @EventHandler
    private void onSave(@EventData("event.target.parentElement.nextElementSibling.value") String updatedNotesText) {
        // there is no easier way to get the value from the textarea so the lengthy event.target EventData is used
        saveNotes(updatedNotesText);
    }

    public void setSaveConsumer(Consumer<String> saveConsumer) {
        this.saveConsumer = saveConsumer;
    }

    public Consumer<String> getSaveConsumer() {
        return saveConsumer;
    }

    protected void saveNotes() {
        saveNotes(notesText);
    }

    protected void saveNotes(String updatedNotesText) {
        saveConsumer.accept(updatedNotesText);
    }

    private boolean canSave(String updatedNotesText) {
        return !Objects.equals(updatedNotesText, notesText) && updatedNotesText.length() <= MAX_NOTES_SIZE;
    }

    public interface Model extends TemplateModel {
        void setTitle(String title);

        void setPlaceholder(String placerholder);

        void setWarning(Boolean warning);

        void setUnsaved(Boolean unsaved);

        void setMax(Integer max);

        void setReadonly(Boolean readonly);

        void setAllowautosave(Boolean allowautosave);

        void setHidesavebutton(Boolean hidesavebutton);

        void setCompact(Boolean compact);

        void setSecondaryStyle(Boolean secondaryStyle);
    }
}
