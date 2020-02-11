package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.TrainingError;
import lombok.EqualsAndHashCode;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainingErrorDAO {
	static final String UNKNOWN_ERROR_KEYWORD = "unknown error";
	private final DSLContext ctx;

	public TrainingErrorDAO(DSLContext ctx) {
		this.ctx = ctx;
	}

	@Cacheable("training_errors_by_keyword")
	public Optional<TrainingError> getErrorByKeyword(String keyword) {
		var error = TrainingErrorRepository.getErrorByKeyword(ctx, keyword);
		if(error == null) {
			error = TrainingErrorRepository.getErrorByKeyword(ctx, UNKNOWN_ERROR_KEYWORD);
		}
		return Optional.ofNullable(error);
	}

	public Optional<TrainingError> getErrorById(long errorId) {
		return Optional.ofNullable(TrainingErrorRepository.getErrorById(ctx, errorId));
	}

	@Cacheable("all_training_errors_keywords")
	public List<String> getAllErrorsKeywords() {
		return TrainingErrorRepository.getAllErrorsKeywords(ctx);
	}
}
