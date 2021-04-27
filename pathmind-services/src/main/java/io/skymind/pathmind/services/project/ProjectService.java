package io.skymind.pathmind.services.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.MetricsDAO;
import io.skymind.pathmind.db.dao.ObservationDAO;
import io.skymind.pathmind.db.dao.PolicyDAO;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.MetricsRaw;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PolicyUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProjectService {

    private final ExperimentDAO experimentDAO;
    private final PolicyDAO policyDAO;
    private final ObservationDAO observationDAO;
    private final MetricsDAO metricsDAO;

    public List<Experiment> getExperiments(Long modelId, Long userId) {

        List<Experiment> experiments = experimentDAO.getExperimentsForModel(modelId, true);
        Map<Long, Long> bestPoliciesId = new ConcurrentHashMap<>(experimentDAO.bestPoliciesForExperiment(modelId));
        Map<Long, List<MetricsRaw>> metricsRaw = metricsDAO.getMetricsRawForPolicy(new ArrayList<>(bestPoliciesId.values()));

        return experiments.parallelStream().peek(experiment -> {
            Long policyId = bestPoliciesId.get(experiment.getId());
            if (policyId != null) {
                Optional<Policy> bestPolicy = policyDAO.getPolicyIfAllowed(policyId, userId);
                bestPolicy.ifPresent(bp -> {
                    experiment.setBestPolicy(bp);
                    experiment.setSelectedObservations(observationDAO.getObservationsForExperiment(experiment.getId()));

                    bp.setMetrics(metricsDAO.getMetricsForPolicy(bp.getId()));
                    bp.setMetricsRaws(metricsRaw.getOrDefault(bp.getId(), List.of()));

                    PolicyUtils.updateSimulationMetricsData(experiment.getBestPolicy());
                    PolicyUtils.updateCompareMetricsChartData(experiment.getBestPolicy());
                });
            }

        }).collect(Collectors.toList());
    }

}
