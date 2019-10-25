package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("stripe-view")
@JsModule("./src/views/stripe-view.js")
@Route(value = "stripe-view")
@JavaScript("https://js.stripe.com/v3")
public class StripeView extends PolymerTemplate<StripeView.Model>
{

	public StripeView()
	{

	}

	public interface Model extends TemplateModel
	{
	}


}
