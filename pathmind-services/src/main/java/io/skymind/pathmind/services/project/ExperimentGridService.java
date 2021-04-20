package io.skymind.pathmind.services.project;

import java.util.List;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperimentGridService {

    private static final int DEFAULT_LIMIT = 10;

    private final ExperimentDAO experimentDAO;

    public List<Experiment> getExperimentsInModelForUser(long userId, long modelId, int offset) {
        return getExperimentsInModelForUser(userId, modelId, offset, DEFAULT_LIMIT);
    }

    public List<Experiment> getExperimentsInModelForUser(long userId, long modelId, int offset, int limit) {
        final List<Experiment> experimentsInModelForUser = experimentDAO.getExperimentsInModelForUser(userId, modelId, offset, limit);
        return experimentsInModelForUser;
    }

    public int countTotalExperimentsInModel(long modelId) {
        return experimentDAO.countExperimentsInModel(modelId);
    }
}
