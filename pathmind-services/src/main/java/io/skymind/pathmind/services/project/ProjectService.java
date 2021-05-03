package io.skymind.pathmind.services.project;

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
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
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

        return experiments.parallelStream().peek(experiment -> {
            final Long policyId = bestPoliciesId.get(experiment.getId());
            if (policyId != null) {
                Optional<Policy> bestPolicy = policyDAO.getPolicyIfAllowed(policyId, userId);
                bestPolicy.ifPresent(bp -> {
                    experiment.setBestPolicy(bp);
                    experiment.setSelectedObservations(observationDAO.getObservationsForExperiment(experiment.getId()));

                    bp.setSimulationMetrics(metricsDAO.getLastIterationMetricsMeanForPolicy(policyId));

                    List<Pair<Double, Double>> rawMetricsAvgVar = metricsDAO.getMetricsRawForPolicy(policyId);

                    bp.setUncertainty(rawMetricsAvgVar.stream()
                            .map(pair -> PathmindNumberUtils.calculateUncertainty(pair.getLeft(), pair.getRight()))
                            .collect(Collectors.toList()));
                });
            }

        }).collect(Collectors.toList());
    }

}
