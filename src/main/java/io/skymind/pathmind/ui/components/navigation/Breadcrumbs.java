package io.skymind.pathmind.ui.components.navigation;

import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.data.Project;
import io.skymind.pathmind.data.Model;
import io.skymind.pathmind.data.Experiment;
import io.skymind.pathmind.ui.views.project.ProjectsView;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;

public class Breadcrumbs extends HorizontalLayout
{
	public Breadcrumbs(Project project) {
		this(project, null, null);
	}

	public Breadcrumbs(Project project, Model model) {
		this(project, model, null);
	}

	public Breadcrumbs(Project project, Model model, Experiment experiment) {
		RouterLink projectsPageLink = createBreadcrumb("Projects", ProjectsView.class, null);
		add(projectsPageLink);

		Span arrow1 = new Span(">");
		add(arrow1);

		RouterLink projectPageLink = createBreadcrumb(project.getName(), ModelsView.class, project.getId());
		add(projectPageLink);

		if (model != null) {
			Span arrow2 = new Span(">");
			add(arrow2);

			RouterLink modelPageLink = createBreadcrumb("Model #" + model.getName(), ExperimentsView.class, model.getId());
			add(modelPageLink);
		}

		if (experiment != null) {
			Span arrow3 = new Span(">");
			add(arrow3);

			RouterLink experimentPageLink = createBreadcrumb("Experiment #" + experiment.getName(), ExperimentView.class, experiment.getId());
			add(experimentPageLink);
		}

		setSpacing(false);
		setAlignItems(Alignment.START);
	}

	private RouterLink createBreadcrumb(String name, Class navigationTarget, Long pageParameter) {
		RouterLink breadcrumb = new RouterLink(name, navigationTarget, pageParameter);
		breadcrumb.addClassName("breadcrumb");
		return breadcrumb;
	}
}