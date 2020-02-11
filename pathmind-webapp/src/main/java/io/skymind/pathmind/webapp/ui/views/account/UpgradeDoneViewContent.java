package io.skymind.pathmind.webapp.ui.views.account;

import io.skymind.pathmind.webapp.security.CurrentUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.db.data.PathmindUser;

@Tag("upgrade-done-view-content")
@JsModule("./src/account/upgrade-done-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UpgradeDoneViewContent extends PolymerTemplate<UpgradeDoneViewContent.Model> {

	private static Logger log = LogManager.getLogger(UpgradeDoneViewContent.class);

	@Id("done")
	private Button done;

	private PathmindUser user;

	@Autowired
	public UpgradeDoneViewContent(CurrentUser currentUser,
								  @Value("${pathmind.contact-support.address}") String contactLink)
	{
		user = currentUser.getUser();
		getModel().setContactLink(contactLink);
		getModel().setPlan("Professional");
		done.addClickListener(e -> UI.getCurrent().navigate(AccountView.class));
	}

	public interface Model extends TemplateModel
	{
		void setContactLink(String contactLink);

		void setPlan(String plan);
	}

}
