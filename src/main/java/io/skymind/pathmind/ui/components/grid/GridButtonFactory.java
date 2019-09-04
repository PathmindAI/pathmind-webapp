package io.skymind.pathmind.ui.components.grid;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.skymind.pathmind.data.Data;
import io.skymind.pathmind.ui.utils.WrapperUtils;

import java.util.function.Consumer;

public class GridButtonFactory
{
	/**
	 * TODO BUG -> It appears as though it's not possible to center a component in a grid unless you
	 * wrap it up around something else like a HorizontalLayout:
	 * https://vaadin.com/forum/thread/17111806/how-to-set-column-alignment-in-grid
	 * TODO -> We should actually create a custom renderer that is re-used but I pushed that off for now.
 	 */
	public static HorizontalLayout getGridButton(String text, ComponentEventListener<ClickEvent<Button>> clickListener) {
		Button button = new Button(text, clickListener);
		button.setThemeName("tertiary-inline");
		return WrapperUtils.wrapCenterAlignmentHorizontal(button);
	}

	public static ComponentRenderer getGridButtonRenderer(Consumer<Data> clickConsumer) {
		return new ComponentRenderer<>(data -> {
			return getGridButton(">", click -> clickConsumer.accept((Data)data));
		});
	}
}
