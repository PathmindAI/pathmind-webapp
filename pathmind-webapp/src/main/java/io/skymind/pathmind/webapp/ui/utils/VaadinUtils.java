package io.skymind.pathmind.webapp.ui.utils;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.InitialPageSettings;

public class VaadinUtils
{
	private VaadinUtils() {
	}

	public static NumberField generateNumberField(long min, long max, long value) {
		NumberField numberField = new NumberField();
		numberField.setHasControls(true);
		numberField.setMin(min);
		numberField.setMax(max);
		numberField.setValue((double)value);
		return numberField;
	}

	/**
	 * Until Vaadin has the ability to get the view names https://github.com/vaadin/flow/issues/1897 this is a workaround.
	 */
	public static String getViewName() {
		return UI.getCurrent().getInternals().getActiveViewLocation().getFirstSegment();
	}

	public static void setupFavIcon(InitialPageSettings settings) {
		settings.addFavIcon("icon", "frontend/images/favicon.png", "32x32");
    }
    
    public static Optional<Element> getElementById(UI ui, String id) {
        if (id == null) return Optional.empty();
        return ui.getElement().getChildren()
                .filter((element) -> id.equals(element.getAttribute("id"))).findFirst();
    }

    public static Supplier<Optional<UI>> convertUiToOptionalSupplier(UI ui) {
	    return () -> Optional.<UI>of(ui);
    }
}
