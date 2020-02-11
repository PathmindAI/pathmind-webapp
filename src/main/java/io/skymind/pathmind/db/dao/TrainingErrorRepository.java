package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.TrainingError;
import org.jooq.DSLContext;

import java.util.List;

import static io.skymind.pathmind.data.db.tables.TrainingError.TRAINING_ERROR;

class TrainingErrorRepository {
	private TrainingErrorRepository() {
	}

	static TrainingError getErrorByKeyword(DSLContext ctx, String keyword) {
		return ctx.select(TRAINING_ERROR.asterisk())
				.from(TRAINING_ERROR)
				.where(TRAINING_ERROR.KEYWORD.eq(keyword))
				.fetchOneInto(TrainingError.class);
	}

	static TrainingError getErrorById(DSLContext ctx, long errorId) {
		return ctx.select(TRAINING_ERROR.asterisk())
				.from(TRAINING_ERROR)
				.where(TRAINING_ERROR.ID.eq(errorId))
				.fetchOneInto(TrainingError.class);
	}

	static List<TrainingError> getAllTrainingErrors(DSLContext ctx) {
		return ctx.select(TRAINING_ERROR.asterisk())
				.from(TRAINING_ERROR)
				.fetchInto(TrainingError.class);
	}
}
