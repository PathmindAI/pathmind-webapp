package io.skymind.pathmind.webapp.ui.components.navigation;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.db.data.Experiment;
import io.skymind.pathmind.db.data.Model;
import io.skymind.pathmind.db.data.Project;
import io.skymind.pathmind.webapp.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.webapp.ui.views.model.ModelView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectView;
import io.skymind.pathmind.webapp.ui.views.project.ProjectsView;

@CssImport(value = "./styles/components/breadcrumbs.css")
public class Breadcrumbs extends HorizontalLayout
{
	private static final String BREADCRUMB_CLASSNAME = "breadcrumb";

	private List<BreadcrumbItem> items = new ArrayList<>();

	public Breadcrumbs() {
		this(null, null, null);
	}

	public Breadcrumbs(Project project) {
		this(project, null, null);
	}

	public Breadcrumbs(Project project, Model model) {
		this(project, model, null);
	}
	public Breadcrumbs(Project project, Model model, Experiment experiment) {
		this(project, model, experiment, true);
	}

	public Breadcrumbs(Project project, Model model, Experiment experiment, boolean hasRootItem) {
		this(project, model, experiment, null, hasRootItem);
	}
	
	public Breadcrumbs(Project project, Model model, Experiment experiment, String stepName) {
		this(project, model, experiment, stepName, true);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Breadcrumbs(Project project, Model model, Experiment experiment, String stepName, boolean hasRootItem) {

		if(hasRootItem) {
			items.add(new BreadcrumbItem("Projects", ProjectsView.class, null));
		}

		if (project != null) {
			items.add(new BreadcrumbItem(project.getName(), ProjectView.class, project.getId()));
		}
		if (model != null) {
			items.add(new BreadcrumbItem("Model #" + model.getName(), ModelView.class, model.getId()));
		}
		if (experiment != null) {
			items.add(new BreadcrumbItem("Experiment #" + experiment.getName(), ExperimentView.class, experiment.getId()));
		}
		
		if (stepName != null) {
			items.add(new BreadcrumbItem(stepName));
		}
		
		items.get(items.size() - 1).asCurrentStep();
		items.forEach(item -> {
			if (getComponentCount() > 0) {
				add(createSeparator());
			}
			add(item.createComponent());
		});

		setSpacing(false);
		setAlignItems(Alignment.START);
	}

	private Span createSeparator() {
		Span separator = new Span(">");
		separator.addClassName("breadcrumb-separator");
		return separator;
	}

	public void setText(int index, String newText) {
		int itemIndex = index > items.size() ? items.size() - 1 : index;
		items.get(itemIndex).setText(newText);
	}
	
	private class BreadcrumbItem<T, C extends Component & HasUrlParameter<T>> {
		private String name;
		private Class<C> navigationTarget;
		private T parameter;
		private boolean isCurrentStep = false;
		protected Span spanComponent;
		protected RouterLink routerLinkComponent;
		
		public BreadcrumbItem(String name) {
			this.name = name;
		}
		
		public BreadcrumbItem(String name, Class<C> navigationTarget, T parameter) {
			this.name = name;
			this.navigationTarget = navigationTarget;
			this.parameter = parameter;
		}
		
		private void asCurrentStep() {
			isCurrentStep = true;
		}
		
		private Component createComponent() {
			if (isCurrentStep || navigationTarget == null) {
				spanComponent = createLabel();
				return spanComponent;
			} else {
				routerLinkComponent = createLink();
				return routerLinkComponent;
			}
		}
		
		private RouterLink createLink() {
			RouterLink routerLink = new RouterLink(name, navigationTarget, parameter);
			routerLink.addClassName(BREADCRUMB_CLASSNAME);
			return routerLink;
		}
		private Span createLabel() {
			Span label = new Span(name);
			label.addClassName(BREADCRUMB_CLASSNAME);
			return label;		
		}

		private void setText(String newText) {
			if (isCurrentStep) {
				spanComponent.setText(newText);
			} else {
				routerLinkComponent.setText(newText);
			}
		}
	}
}