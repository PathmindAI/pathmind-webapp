package io.skymind.pathmind.ui.components.navigation.breadcrumbs;

import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import io.skymind.pathmind.ui.views.project.ProjectsView;
import io.skymind.pathmind.ui.views.model.ModelsView;
import io.skymind.pathmind.ui.views.experiment.ExperimentsView;
import io.skymind.pathmind.ui.views.experiment.ExperimentView;

public class Breadcrumbs extends HorizontalLayout
{
	public Breadcrumbs(BreadcrumbsData breadcrumbsData) {
		RouterLink projectsPageLink = createBreadcrumb("Projects", ProjectsView.class, null);
		add(projectsPageLink);

		RouterLink projectPageLink = createBreadcrumb(breadcrumbsData.getProjectName(), ModelsView.class, breadcrumbsData.getProjectId());
		add(projectPageLink);

		if (breadcrumbsData.getModelNumber() != null) {
			RouterLink modelPageLink = createBreadcrumb("Model #" + breadcrumbsData.getModelNumber(), ExperimentsView.class, breadcrumbsData.getModelId());
			add(modelPageLink);
		}

		if (breadcrumbsData.getExperimentNumber() != null) {
			RouterLink experimentPageLink = createBreadcrumbExperiment("Experiment #" + breadcrumbsData.getExperimentNumber(), ExperimentView.class, breadcrumbsData.getExperimentPageParameter());
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

	private RouterLink createBreadcrumbExperiment(String name, Class navigationTarget, String pageParameter) {
		RouterLink breadcrumb = new RouterLink(name, navigationTarget, pageParameter);
		breadcrumb.addClassName("breadcrumb");
		return breadcrumb;
	}
}
