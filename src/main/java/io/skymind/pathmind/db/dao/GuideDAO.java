package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.constants.GuideStep;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class GuideDAO
{
	private final DSLContext ctx;

	GuideDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	public GuideStep getGuideStep(long projectId) {
		return GuideRepository.getGuideStep(ctx, projectId);
	}

	public void updateGuideStep(long projectId, GuideStep guideStep) {
		GuideRepository.updateGuideStep(ctx, projectId, guideStep);
	}
}
