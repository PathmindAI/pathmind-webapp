package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.TrainingError;
import org.jooq.DSLContext;

import static io.skymind.pathmind.data.db.tables.TrainingError.TRAINING_ERROR;

public class TrainingErrorRepository {

	public static TrainingError getErrorByDescription(DSLContext ctx, String description) {
		return ctx.select(TRAINING_ERROR.ID, TRAINING_ERROR.DESCRIPTION, TRAINING_ERROR.ADVICE, TRAINING_ERROR.REPEATABLE)
				.from(TRAINING_ERROR)
				.where(TRAINING_ERROR.DESCRIPTION.eq(description))
				.fetchOneInto(TrainingError.class);
	}

	public static TrainingError getErrorById(DSLContext ctx, long errorId) {
		return ctx.select(TRAINING_ERROR.ID, TRAINING_ERROR.DESCRIPTION, TRAINING_ERROR.ADVICE, TRAINING_ERROR.REPEATABLE)
				.from(TRAINING_ERROR)
				.where(TRAINING_ERROR.ID.eq(errorId))
				.fetchOneInto(TrainingError.class);
	}
}
