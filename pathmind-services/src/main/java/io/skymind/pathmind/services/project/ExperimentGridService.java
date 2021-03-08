package io.skymind.pathmind.services.project;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.data.DashboardItem;
import io.skymind.pathmind.shared.data.Experiment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ExperimentGridService {

    private final ExperimentDAO experimentDAO;

    public int countTotalExperimentsInModel(long modelId) {
        return experimentDAO.countExperimentsInModel(modelId);
    }
}
