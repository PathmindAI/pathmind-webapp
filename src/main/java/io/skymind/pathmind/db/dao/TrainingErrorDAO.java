package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.TrainingError;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.skymind.pathmind.data.db.tables.TrainingError.TRAINING_ERROR;

@Repository
public class TrainingErrorDAO {
	private static final String UNKNOWN_ERROR_MESSAGE = "unknown error";
	private final DSLContext ctx;

	public TrainingErrorDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	public Optional<TrainingError> getErrorByDescription(String description) {
		var error = TrainingErrorRepository.getErrorByDescription(ctx, description);
		if(error == null) {
			error = TrainingErrorRepository.getErrorByDescription(ctx, UNKNOWN_ERROR_MESSAGE);
		}
		return Optional.ofNullable(error);
	}

	public Optional<TrainingError> getErrorById(long errorId) {
		return Optional.ofNullable(TrainingErrorRepository.getErrorById(ctx, errorId));
	}
}
