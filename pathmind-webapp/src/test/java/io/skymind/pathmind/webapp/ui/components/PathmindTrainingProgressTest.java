package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.progressbar.ProgressBar;
import org.junit.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.Assert.assertEquals;

public class PathmindTrainingProgressTest {

	@Test
	public void testProgressTextFormatting() {

		PathmindTrainingProgress pathmindTrainingProgress = new PathmindTrainingProgress();
		pathmindTrainingProgress.setValue(10, 100);

		// locate nested ProgressBar and Span by using _get from Karibu
		ProgressBar progressBar = _get(pathmindTrainingProgress, ProgressBar.class);
		Span span = _get(pathmindTrainingProgress, Span.class);

		assertEquals(progressBar.getValue(), 10, 0);
		assertEquals("10% (ETA: 2 min)", span.getText());
	}
}