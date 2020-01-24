package io.skymind.pathmind.ui.views.guide;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.security.CurrentUser;
import io.skymind.pathmind.ui.views.model.UploadModelView;

@Tag("guide-overview-content")
@JsModule("./src/guide/guide-overview-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GuideOverviewContent extends PolymerTemplate<GuideOverviewContent.Model> {

	@Id("nextBtn")
	private Button nextBtn;

	@Id("skipToUploadModelBtn")
	private Button skipToUploadModelBtn;

	@Autowired
	public GuideOverviewContent(CurrentUser currentUser) {
	}

	@PostConstruct
	private void init() {
		initBtns();
	}

	private void initBtns() {
		// Fake project
		long projectId = 3;

		nextBtn.addClickListener(e -> UI.getCurrent().navigate(InstallPathmindHelperView.class));
		skipToUploadModelBtn.addClickListener(e -> UI.getCurrent().navigate(UploadModelView.class, projectId));
	}

	public interface Model extends TemplateModel {
	}
}
