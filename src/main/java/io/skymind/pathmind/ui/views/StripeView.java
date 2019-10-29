package io.skymind.pathmind.ui.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import io.skymind.pathmind.services.billing.StripeService;
import io.skymind.pathmind.ui.layouts.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Tag("stripe-view")
@JsModule("./src/views/stripe-view.js")
@JsModule("@power-elements/stripe-elements")
@Route(value = "stripe-view", layout = MainLayout.class)
@JavaScript("https://js.stripe.com/v3")
@CssImport(value = "./styles/views/stripe-view.css")
public class StripeView extends PolymerTemplate<StripeView.Model>
{

	private StripeService stripeService;
	private final String publicKey;

	@Id("submit")
	private Button submit;

	public StripeView(@Value("${pathmind.stripe.public.key}")String publicKey, @Autowired StripeService stripeService)
	{
		this.publicKey = publicKey;
		this.stripeService = stripeService;
	}

	@PostConstruct
	private void init()
	{
		getModel().setKey(publicKey);
	}

	@EventHandler
	private void submit() {
		//this.getElement().executeJs("$element");

	}

	public interface Model extends TemplateModel
	{
		void setKey(String key);
		Boolean getReady();
		String getToken();
	}


}
