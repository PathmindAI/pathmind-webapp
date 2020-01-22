package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.constants.GuideStep;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class GuideDAO
{
	private final DSLContext ctx;

	// STEPH -> TODO -> Remove once implemented.
	private static final HashMap<Long, GuideStep> memory = new HashMap<>();

	GuideDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	// STEPH -> TODO -> Implement
	public void updateGuideStep(long projectId, GuideStep guideStep) {
		memory.put(projectId, guideStep);
	}

	// STEPH -> TODO -> Implement
	public GuideStep getGuideStep(long projectId) {
		GuideStep guideStep = memory.get(projectId);
		// We have several options here:
		// 		1. The guideStep is part of the user table in which case we can add a default value of GuideStep.Overview and enforce not null to the column.
		// 		2. The guideStep is a separate table so that it can be expanded a lot. We then need to also insert a new default row entry each time a new account is created. Not null constraint it still in effect.
		// 		3. We add the null check in the UI or DAO layer. Least recommended option since it removes the null check constraint from the database.
		return guideStep == null ? GuideStep.Overview : guideStep;
	}
}
