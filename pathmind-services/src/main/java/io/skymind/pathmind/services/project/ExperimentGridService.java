package io.skymind.pathmind.services.project;

import java.util.List;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.shared.data.Experiment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperimentGridService {

    private final ExperimentDAO experimentDAO;

    public List<Experiment> getExperimentsInModelForUser(long userId, long modelId, boolean isArchived, int offset, int limit) {
        final List<Experiment> experimentsInModelForUser = experimentDAO.getExperimentsInModelForUser(userId, modelId, isArchived, offset, limit);
        return experimentsInModelForUser;
    }

    public int countFilteredExperimentsInModel(long modelId, boolean isArchived) {
        return experimentDAO.countFilteredExperimentsInModel(modelId, isArchived);
    }
}
