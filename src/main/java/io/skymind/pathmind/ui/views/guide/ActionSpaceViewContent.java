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

@Tag("action-space-view-content")
@JsModule("./src/guide/action-space-view-content.js")
@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ActionSpaceViewContent extends PolymerTemplate<ActionSpaceViewContent.Model> {

    @Id("backBtn")
    private Button backBtn;

    @Id("nextBtn")
    private Button nextBtn;

    @Autowired
    public ActionSpaceViewContent(CurrentUser currentUser) {
    }

    @PostConstruct
    private void init() {
    }

    public interface Model extends TemplateModel {
    }
}
