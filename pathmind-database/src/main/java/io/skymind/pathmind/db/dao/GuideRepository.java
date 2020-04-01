package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.constants.GuideStep;
import io.skymind.pathmind.db.dao.enumConverter.GuideStepEnumConverter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.tables.Guide.GUIDE;

@Slf4j
class GuideRepository
{
	protected static GuideStep getGuideStep(DSLContext ctx, long projectId) {
		return ctx.select(GUIDE.STEP)
				.from(GUIDE)
				.where(GUIDE.PROJECT_ID.eq(projectId))
				.fetchOne(0, new GuideStepEnumConverter());
}

	protected static void updateGuideStep(DSLContext ctx, long projectId, GuideStep guideStep) {
		ctx.update(GUIDE)
				.set(GUIDE.STEP, guideStep.getValue())
				.where(GUIDE.PROJECT_ID.eq(projectId))
				.execute();
	}

	protected static void insertGuideStep(DSLContext ctx, long projectId, GuideStep guideStep) {
		ctx.insertInto(GUIDE)
				.set(GUIDE.PROJECT_ID, projectId)
				.set(GUIDE.STEP, guideStep.getValue())
				.execute();
	}
}
