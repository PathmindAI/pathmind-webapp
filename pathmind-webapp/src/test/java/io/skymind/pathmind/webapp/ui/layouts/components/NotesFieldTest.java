package io.skymind.pathmind.webapp.ui.layouts.components;

import io.skymind.pathmind.webapp.ui.components.molecules.NotesField;
import io.skymind.pathmind.webapp.ui.karibu.KaribuUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NotesFieldTest {

    private NotesField notesField;

    @Before
    public void setUp() {
        KaribuUtils.setup();
        notesField = new NotesField("title", "abc", text -> {}, false, true, false);
    }

    @After
    public void tearDown() {
        KaribuUtils.tearDown();
    }

    @Test
    public void notesFieldTextTest() {
        assertEquals("abc", notesField.getNotesText());
    }
    
    @Test
    public void notesFieldSaveButtonTest() {
        // Check save button is clickable on the server side. This is not 100% reliable because the button may still be invisible on the client side.
        // The Karibu _click method would throw IllegalStateException if the button was not visible or not enabled on the server side.
        // For example, if the test is run after this line is executed: notesField.save.setEnabled(false);
        _click(notesField.save);
    }
    
    @Test
    public void notesFieldTextChangeTest() {
        assertEquals("abc", notesField.getNotesText());
        notesField.setNotesText("Changed the text haha");
        assertEquals("Changed the text haha", notesField.getNotesText());
        assertNotEquals("abc", notesField.getNotesText());
    }

}