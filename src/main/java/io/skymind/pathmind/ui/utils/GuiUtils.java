package io.skymind.pathmind.ui.utils;

import com.vaadin.flow.component.html.Hr;

public class GuiUtils
{
	public static Hr getFullWidthHr() {
		Hr hr = new Hr();
		hr.setWidthFull();
		return hr;
	}

	public static Hr getHr(String width) {
		Hr hr = new Hr();
		hr.setWidth(width);
		return hr;
	}
}
