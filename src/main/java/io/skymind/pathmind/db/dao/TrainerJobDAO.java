package io.skymind.pathmind.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.constants.RunStatus;
import io.skymind.pathmind.data.TrainerJob;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerJobDAO {
    private final DSLContext ctx;
    private final ObjectMapper mapper;

    public TrainerJobDAO(DSLContext ctx, ObjectMapper mapper) {
        this.ctx = ctx;
        this.mapper = mapper;
    }

    public TrainerJob getTrainerJob(String trainerJobId) {
        return TrainerJobRepository.getTrainerJob(ctx, trainerJobId);
    }

    public RunStatus getStatus(String trainerJobId) {
        return TrainerJobRepository.getStatus(ctx, trainerJobId);
    }
}
