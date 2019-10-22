package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.router.Route;

@Tag("stripe-view")
@Route("stripe-view")
@JsModule("./src/views/stripe-test.js")
public class StripeView extends Component
{

	public StripeView()
	{

	}


}
