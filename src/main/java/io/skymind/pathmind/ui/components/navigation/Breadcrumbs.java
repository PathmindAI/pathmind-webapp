package io.skymind.pathmind.ui.components.navigation;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.experiment.utils.ExperimentViewNavigationUtils;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.project.ProjectsView;

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
	
	public Breadcrumbs(Project project, Model model, Experiment experiment, boolean hasRootItem) {
		if (hasRootItem) {
			RouterLink projectsPageLink = createBreadcrumb("Projects", ProjectsView.class, null);
			add(projectsPageLink);
		}

		if (project != null) {
			if (hasRootItem) {
				add(createSeparator());	
			}
			add(createBreadcrumb(project.getName(), ModelsView.class, project.getId()));
		}
		if (model != null) {
			add(createSeparator());
			add(createBreadcrumb("Model #" + model.getName(), ExperimentsView.class, model.getId()));
		}
		if (experiment != null) {
			add(createSeparator());
			add(createBreadcrumbExperiment("Experiment #" + experiment.getName(), ExperimentView.class, ExperimentViewNavigationUtils.getExperimentParameters(experiment)));
		}

		setSpacing(false);
		setAlignItems(Alignment.START);
	}

	private RouterLink createBreadcrumb(String name, Class navigationTarget, Long pageParameter) {
		RouterLink breadcrumb = new RouterLink(name, navigationTarget, pageParameter);
		breadcrumb.addClassName(BREADCRUMB_CLASSNAME);
		return breadcrumb;
	}

	private RouterLink createBreadcrumbExperiment(String name, Class navigationTarget, String pageParameter) {
		RouterLink breadcrumb = new RouterLink(name, navigationTarget, pageParameter);
		breadcrumb.addClassName(BREADCRUMB_CLASSNAME);
		return breadcrumb;
	}

	private Span createSeparator() {
		return new Span(">");
	}
}