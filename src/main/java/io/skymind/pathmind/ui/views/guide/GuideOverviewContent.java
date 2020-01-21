package io.skymind.pathmind.ui.views.guide;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.security.CurrentUser;

@Tag("guide-overview-content")
@JsModule("./src/guide/guide-overview-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GuideOverviewContent extends PolymerTemplate<GuideOverviewContent.Model> {

	@Id("skipToUploadModelBtn")
	private Button skipToUploadModelBtn;

	@Id("nextBtn")
	private Button nextBtn;

	@Autowired
	public GuideOverviewContent(CurrentUser currentUser) {
		initContent();
	}

	@PostConstruct
	private void init() {
	}

	private void initContent() {
		getModel().setTitle("Guide to preparing your simulation for Pathmind");
	}

	public interface Model extends TemplateModel {
		void setTitle(String title);
	}
}
