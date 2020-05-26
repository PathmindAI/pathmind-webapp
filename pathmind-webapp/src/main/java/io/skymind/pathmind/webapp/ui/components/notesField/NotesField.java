package io.skymind.pathmind.webapp.ui.components.notesField;

import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

import io.skymind.pathmind.webapp.ui.components.LabelFactory;

@CssImport("./styles/components/notes-field.css")
public class NotesField extends HorizontalLayout {

    private static final int MAX_NOTES_SIZE = 1000;

    private String title;
    private String notesText;
    private Button saveButton;
    private TextArea blockEditableField;
    private Span hintWrapper;
    private Span warningWrapper;
    private Icon savedIcon;
    private Consumer<String> saveConsumer;

    public NotesField(String title, String notesText, Consumer<String> saveConsumer) {
        this.notesText = notesText;
        this.title = title;
        this.saveConsumer = saveConsumer;
        hintWrapper = LabelFactory.createLabel("Unsaved Notes!", "hint-label");
        hintWrapper.setVisible(false);
        savedIcon = new Icon(VaadinIcon.CHECK);
        savedIcon.setVisible(false);
        warningWrapper = LabelFactory.createLabel(String.format("Notes must not exceed %s characters", MAX_NOTES_SIZE), "hint-label", "unsaved-and-too-big-text-label");
        warningWrapper.setVisible(false);
        add(editableFieldWrapper());
        setSpacing(false);
        addClassName("notes-field-wrapper");
    }

    private void updateInformationShownToUser(String notesValue) {
        warningWrapper.setVisible(false);
        hintWrapper.setVisible(false);
        savedIcon.setVisible(false);
        if (notesValue.length() > MAX_NOTES_SIZE) {
            warningWrapper.setVisible(true);
        }
        else if (!Objects.equals(notesText, notesValue)) {
            hintWrapper.setVisible(true);
        }
    }

    private void createEditableField() {
        blockEditableField = new TextArea("", StringUtils.defaultString(notesText), "Add Notes");
        // make sure the value change event will be triggered whenever the user type something
        blockEditableField.setValueChangeMode(ValueChangeMode.EAGER);
        blockEditableField.addThemeName("notes");
        blockEditableField.addBlurListener(event -> {
            if (canSave(blockEditableField.getValue())) {
                saveButton.click();
                hintWrapper.setVisible(false);
                savedIcon.setVisible(true);
                savedIcon.getElement().executeJs("$0.classList.add('fade-in'); setTimeout(() => {$0.classList.remove('fade-in');}, 3000)");
            }
        });
        blockEditableField.addValueChangeListener(event -> {
            updateInformationShownToUser(blockEditableField.getValue());
        });
    }

    private VerticalLayout editableFieldWrapper() {
        initButtons();
        Span spanTitle = new Span(title);
        spanTitle.getStyle().set("flex", "1");
        HorizontalLayout headerRow = new HorizontalLayout(
            spanTitle,
            hintWrapper,
            warningWrapper,
            savedIcon,
            saveButton
        );
        headerRow.setSpacing(false);
        headerRow.addClassName("header");
        createEditableField();
        VerticalLayout editableFieldWrapper = new VerticalLayout(
            headerRow,
            blockEditableField
        );
        editableFieldWrapper.addClassName("notes-block");
        editableFieldWrapper.setSpacing(false);
        editableFieldWrapper.setPadding(false);
        return editableFieldWrapper;
    }

    private void initButtons() {
        saveButton = new Button("Save");
        saveButton.addClickListener(e -> {
            saveButtonOnClick();
        });
    }

    public void setNotesText(String notesText) {
        this.notesText = notesText;
        blockEditableField.setValue(notesText);
    }

    public void setPlaceholder(String placeholderText) {
        blockEditableField.setPlaceholder(placeholderText);
    }

    public void saveButtonOnClick() {
        String updatedNotesText = blockEditableField.getValue();
        if (canSave(updatedNotesText)){
            warningWrapper.setVisible(false);
            hintWrapper.setVisible(false);
            savedIcon.setVisible(true);
            notesText = updatedNotesText;
            saveConsumer.accept(updatedNotesText);
        }
    }

    private boolean canSave(String updatedNotesText) {
        return !Objects.equals(updatedNotesText, notesText) && updatedNotesText.length() <= MAX_NOTES_SIZE;
    }
}
