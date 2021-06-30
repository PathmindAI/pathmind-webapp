package io.skymind.pathmind.db.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.skymind.pathmind.shared.data.TrainingError;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.shared.services.training.constant.ErrorConstants.NOT_AN_ERROR;
import static io.skymind.pathmind.shared.services.training.constant.ErrorConstants.UNKNOWN_ERROR_KEYWORD;

@Repository
public class TrainingErrorDAO {
    private final DSLContext ctx;

    public TrainingErrorDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Cacheable("training_errors_by_keyword")
    public Optional<TrainingError> getErrorByKeyword(String keyword) {
        var error = TrainingErrorRepository.getErrorByKeyword(ctx, keyword);
        if (error == null) {
            error = TrainingErrorRepository.getErrorByKeyword(ctx, UNKNOWN_ERROR_KEYWORD);
        }
        return Optional.ofNullable(error);
    }

    @Cacheable("all_training_errors_keywords")
    public List<String> getAllErrorsKeywords() {
        return TrainingErrorRepository.getAllErrorsKeywords(ctx);
    }

    public List<TrainingError> getAllKnownErrorsKeywords() {
        final var excludeMessages = List.of(UNKNOWN_ERROR_KEYWORD, NOT_AN_ERROR);
        return TrainingErrorRepository.getAllKnownErrors(ctx).stream()
                .filter(error -> !excludeMessages.contains(error.getKeyword()))
                .collect(Collectors.toList());
    }
}
