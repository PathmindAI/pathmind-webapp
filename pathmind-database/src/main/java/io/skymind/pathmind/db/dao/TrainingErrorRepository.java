package io.skymind.pathmind.db.dao;

import java.util.List;

import io.skymind.pathmind.shared.data.TrainingError;
import org.jooq.DSLContext;

import static io.skymind.pathmind.db.jooq.tables.TrainingError.TRAINING_ERROR;

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

    static List<String> getAllErrorsKeywords(DSLContext ctx) {
        return ctx.select(TRAINING_ERROR.KEYWORD)
                .from(TRAINING_ERROR)
                .orderBy(TRAINING_ERROR.ID)
                .fetchInto(String.class);
    }

    static List<TrainingError> getAllKnownErrors(DSLContext ctx) {
        return ctx.select(TRAINING_ERROR.asterisk())
            .from(TRAINING_ERROR)
            .orderBy(TRAINING_ERROR.ID)
            .fetchInto(TrainingError.class);
    }
}
