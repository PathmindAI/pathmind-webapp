package io.skymind.pathmind.webapp.ui.components.molecules;

import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.flow.component.EventData;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("notes-field")
@JsModule("./src/components/molecules/notes-field.js")
public class NotesField extends PolymerTemplate<NotesField.Model> {

    private static final int MAX_NOTES_SIZE = 1000;

    private String notesText;
    private Consumer<String> saveConsumer;

    public NotesField(String title, String notesText, Consumer<String> saveConsumer) {
        this.saveConsumer = saveConsumer;
        setNotesText(notesText);
        getModel().setTitle(title);
        getModel().setMax(MAX_NOTES_SIZE);
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
        getModel().setNotes(notesText);
    }

    public void setPlaceholder(String placeholder) {
        getModel().setPlaceholder(placeholder);
    }

    public void setReadonly(Boolean readonly) {
        getModel().setReadonly(readonly);
    }

    @EventHandler
    private void onSave(@EventData("event.target.parentElement.nextElementSibling.value") String updatedNotesText) {
        // there is no easier way to get the value from the textarea so the lengthy event.target EventData is used
        if (canSave(updatedNotesText)) {
            notesText = updatedNotesText;
            saveConsumer.accept(updatedNotesText);
        }
    }

    private boolean canSave(String updatedNotesText) {
        return !Objects.equals(updatedNotesText, notesText) && updatedNotesText.length() <= MAX_NOTES_SIZE;
    }

	public interface Model extends TemplateModel {
        void setTitle(String title);
        void setPlaceholder(String placerholder);
        void setNotes(String notes);
        void setWarning(Boolean warning);
        void setUnsaved(Boolean unsaved);
        void setMax(Integer max);
        void setReadonly(Boolean readonly);
	}
}
