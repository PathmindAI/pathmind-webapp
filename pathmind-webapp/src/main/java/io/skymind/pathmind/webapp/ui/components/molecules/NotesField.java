package io.skymind.pathmind.webapp.ui.components.molecules;

import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.server.Command;

@Tag("notes-field")
@JsModule("./src/components/molecules/notes-field.ts")
public class NotesField extends LitTemplate {
    @Id("save")
	public Button save;

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
        getElement().setProperty("title", title);
        getElement().setProperty("max", MAX_NOTES_SIZE);
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
        getElement().setProperty("placeholder", placeholder);
    }

    public void setReadonly(Boolean readonly) {
        getElement().setProperty("readonly", readonly);
        save.setEnabled(!readonly);
    }

    public void setAllowAutoSave(Boolean allowAutoSave) {
        getElement().setProperty("allowautosave", allowAutoSave);
    }

    public void setHideSaveButton(Boolean hideSaveButton) {
        getElement().setProperty("hidesavebutton", hideSaveButton);
    }

    public void setCompact(Boolean compact) {
        getElement().setProperty("compact", compact);
    }

    public void setSecondaryStyle(Boolean secondaryStyle) {
        getElement().setProperty("secondaryStyle", secondaryStyle);
    }

    public void setOnNotesChangeHandler(Command onNotesChangeHandler) {
        this.onNotesChangeHandler = onNotesChangeHandler;
    }

    @ClientCallable
    private void onNotesChange(@EventData("event.target.value") String updatedNotesText) {
        if (canSave(updatedNotesText)) {
            notesText = updatedNotesText;
            onNotesChangeHandler.execute();
        }
    }

    @ClientCallable
    private void onSave(String updatedNotesText) {
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
}
