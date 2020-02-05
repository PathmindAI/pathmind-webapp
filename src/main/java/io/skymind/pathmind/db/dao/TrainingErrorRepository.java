package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.TrainingError;
import org.jooq.DSLContext;

import static io.skymind.pathmind.data.db.tables.TrainingError.TRAINING_ERROR;

public class TrainingErrorRepository {
	private TrainingErrorRepository() {
	}

	static TrainingError getErrorByKeyword(DSLContext ctx, String keyword) {
		return ctx.select(TRAINING_ERROR.ID, TRAINING_ERROR.KEYWORD, TRAINING_ERROR.DESCRIPTION, TRAINING_ERROR.RESTARTABLE)
				.from(TRAINING_ERROR)
				.where(TRAINING_ERROR.KEYWORD.eq(keyword))
				.fetchOneInto(TrainingError.class);
	}

	static TrainingError getErrorById(DSLContext ctx, long errorId) {
		return ctx.select(TRAINING_ERROR.ID, TRAINING_ERROR.KEYWORD, TRAINING_ERROR.DESCRIPTION, TRAINING_ERROR.RESTARTABLE)
				.from(TRAINING_ERROR)
				.where(TRAINING_ERROR.ID.eq(errorId))
				.fetchOneInto(TrainingError.class);
	}
}
