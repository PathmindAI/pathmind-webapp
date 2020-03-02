package io.skymind.pathmind.ui.components.navigation;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.ProjectsView;

@CssImport(value = "./styles/components/breadcrumbs.css")
public class Breadcrumbs extends HorizontalLayout
{
	private static final String BREADCRUMB_CLASSNAME = "breadcrumb";

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

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Breadcrumbs(Project project, Model model, Experiment experiment, boolean hasRootItem) {
		List<BreadcrumbItem> items = new ArrayList<>();

		if(hasRootItem) {
			items.add(new BreadcrumbItem("Projects", ProjectsView.class, null));
		}

		if (project != null) {
			items.add(new BreadcrumbItem(project.getName(), ModelsView.class, project.getId()));
		}
		if (model != null) {
			items.add(new BreadcrumbItem("Model #" + model.getName(), ExperimentsView.class, model.getId()));
		}
		if (experiment != null) {
			items.add(new BreadcrumbItem("Experiment #" + experiment.getName(), ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(experiment)));
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
	
	private class BreadcrumbItem<T, C extends Component & HasUrlParameter<T>> {
		private String name;
		private Class<C> navigationTarget;
		private T parameter;
		private boolean isCurrentStep = false;
		
		public BreadcrumbItem(String name, Class<C> navigationTarget, T parameter) {
			this.name = name;
			this.navigationTarget = navigationTarget;
			this.parameter = parameter;
		}
		
		private void asCurrentStep() {
			isCurrentStep = true;
		}
		
		private Component createComponent() {
			if (isCurrentStep) {
				return createLabel();
			} else {
				return createLink();
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
	}
}