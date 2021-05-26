package io.skymind.pathmind.services.project;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.db.utils.GridSortOrder;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.utils.PathmindNumberUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperimentGridService {

    private final ExperimentDAO experimentDAO;
    private final RunDAO runDAO;

    private final Set<String> experimentSortingFields = Set.of("NAME", "DATE_CREATED", "TRAINING_STATUS");


    public List<Experiment> getExperimentsInModelForUser(long userId, long modelId, boolean isArchived, int offset, int limit, List<GridSortOrder> sortOrders) {
        final String sortBy = sortOrders.size() > 0 ? sortOrders.get(0).getPropertyName() : "";
        final boolean isDesc = sortOrders.size() > 0 && sortOrders.get(0).isDescending();

        boolean inAppSorting = !StringUtils.isEmpty(sortBy) && !experimentSortingFields.contains(sortBy.toUpperCase());

        int originOffset = offset;
        int originLimit = limit;

        if (inAppSorting) {
            offset = 0;
            limit = Integer.MAX_VALUE;
            sortOrders = List.of();
        }

        List<Experiment> experiments = experimentDAO.getExperimentsInModelForUser(userId, modelId, isArchived, offset, limit, sortOrders);

        if(inAppSorting && !experiments.isEmpty()) {
            int rewardVariableIndex = Integer.parseInt(sortBy);

            Comparator<Experiment> metricsComparator = new MetricsComparator(rewardVariableIndex);
            if (isDesc) {
                metricsComparator = metricsComparator.reversed();
            }
            experiments = experiments.stream().sorted(metricsComparator).skip(originOffset).limit(originLimit).collect(Collectors.toList());
        }

        return experiments;
    }

    public int countFilteredExperimentsInModel(long modelId, boolean isArchived) {
        return experimentDAO.countFilteredExperimentsInModel(modelId, isArchived);
    }


    static class MetricsComparator implements Comparator<Experiment> {

        private final ToDoubleFunction<Experiment> keyExtractor;

        MetricsComparator(int rewardVarIndex) {
            keyExtractor = experiment -> {
                if (experiment.getBestPolicy() != null) {
                    Policy bestPolicy = experiment.getBestPolicy();
                    return bestPolicy.getUncertainty() != null && !bestPolicy.getUncertainty().isEmpty()
                            ? Double.parseDouble(bestPolicy.getUncertainty().get(rewardVarIndex).split("\u2800\u00B1\u2800")[0])
                            : Double.parseDouble(PathmindNumberUtils.formatNumber(bestPolicy.getSimulationMetrics().get(rewardVarIndex)));
                }
                return Double.NEGATIVE_INFINITY;
            };
        }

        @Override
        public int compare(Experiment e1, Experiment e2) {
            return Double.compare(keyExtractor.applyAsDouble(e1), keyExtractor.applyAsDouble(e2));
        }
    }

}
