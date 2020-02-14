package io.skymind.pathmind.ui.views.guide.template;

import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

import io.skymind.pathmind.constants.GuideStep;
import io.skymind.pathmind.db.dao.GuideDAO;
import io.skymind.pathmind.ui.plugins.SegmentIntegrator;

public class DefaultPageContent<M extends TemplateModel> extends PolymerTemplate<TemplateModel> {
	protected void initBtns(GuideDAO guideDAO, GuideStep guideStep, long projectId, SegmentIntegrator segmentIntegrator) {
	}
}
