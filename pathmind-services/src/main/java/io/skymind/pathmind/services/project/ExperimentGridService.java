package io.skymind.pathmind.services.project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import io.skymind.pathmind.db.dao.ExperimentDAO;
import io.skymind.pathmind.db.dao.RewardVariableDAO;
import io.skymind.pathmind.db.utils.GridSortOrder;
import io.skymind.pathmind.shared.constants.GoalConditionType;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Policy;
import io.skymind.pathmind.shared.data.RewardVariable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import static io.skymind.pathmind.shared.constants.GoalConditionType.LESS_THAN_OR_EQUAL;
import static io.skymind.pathmind.shared.utils.PathmindNumberUtils.removeThousandsSeparatorFromNumber;

@Service
@RequiredArgsConstructor
public class ExperimentGridService {

    private final ExperimentDAO experimentDAO;

    private final RewardVariableDAO rewardVariableDAO;

    private final Set<String> experimentSortingFields = Set.of("NAME", "DATE_CREATED", "TRAINING_STATUS");

    public List<Experiment> getExperimentsInModelForUser(long userId, long modelId, boolean isArchived, int offset, int limit, List<GridSortOrder> sortOrders) {
        final String sortBy = sortOrders.size() > 0 ? sortOrders.get(0).getPropertyName() : "";
        final boolean isDesc = sortOrders.size() > 0 && sortOrders.get(0).isDescending();

        boolean inAppSorting = !StringUtils.isEmpty(sortBy) && !experimentSortingFields.contains(sortBy.toUpperCase());

        int originOffset = offset;
        int originLimit = limit;

        offset = 0;
        limit = Integer.MAX_VALUE;
        if (inAppSorting) {
            sortOrders = List.of();
        }

        List<Experiment> experiments = experimentDAO.getExperimentsInModelForUser(userId, modelId, isArchived, offset, limit, sortOrders);

        Map<Integer, Pair<Double, Double>> rewardVariablesMinMax = new HashMap<>();

        for (Experiment experiment : experiments) {
            final Policy bestPolicy = experiment.getBestPolicy();
            if (bestPolicy != null) {
                final int size = bestPolicy.getMetricDisplayValues().size();
                for (int i = 0; i < size; i++) {
                    final String valueWithUncertainty = bestPolicy.getMetricDisplayValues().get(i);
                    Double value = parseRewardVariableValue(valueWithUncertainty);
                    Pair<Double, Double> minMax = rewardVariablesMinMax.get(i);
                    if (minMax == null) {
                        minMax = Pair.of(value, value);
                    } else {
                        minMax = Pair.of(
                                Math.min(value, minMax.getLeft()),
                                Math.max(value, minMax.getRight())
                        );
                    }
                    rewardVariablesMinMax.put(i, minMax);
                }
            }
        }

        if (inAppSorting && !experiments.isEmpty()) {
            int rewardVariableIndex = Integer.parseInt(sortBy.replace("reward_var_", ""));

            Comparator<Experiment> metricsComparator = new MetricsComparator(rewardVariableIndex);
            if (isDesc) {
                metricsComparator = metricsComparator.reversed();
            }
            experiments = experiments.stream().sorted(metricsComparator).collect(Collectors.toList());
        }
        experiments = experiments.stream().skip(originOffset).limit(originLimit).collect(Collectors.toList());


        List<RewardVariable> rewardVariables = rewardVariableDAO.getRewardVariablesForModel(modelId);

        for (Experiment experiment : experiments) {
            final Policy bestPolicy = experiment.getBestPolicy();
            List<Double> scores = new ArrayList<>();
            for (int i = 0; i < rewardVariables.size(); i++) {
                scores.add(null);
                if (bestPolicy != null && bestPolicy.getMetricDisplayValues().size() > i) {

                    RewardVariable rewardVariable = rewardVariables.get(i);
                    final GoalConditionType goalCondition = rewardVariable.getGoalConditionTypeEnum();
                    if (goalCondition == null) {
                        // skip calculation if no RW goal direction
                        continue;
                    }

                    Double score = null;
                    final String valueWithUncertainty = bestPolicy.getMetricDisplayValues().get(i);
                    final Double value = parseRewardVariableValue(valueWithUncertainty);
                    final Pair<Double, Double> minMax = rewardVariablesMinMax.get(i);
                    if (minMax != null && !minMax.getLeft().equals(minMax.getRight())) {
                        score = (value - minMax.getLeft()) / Math.abs(minMax.getRight() - minMax.getLeft());
                        if (goalCondition == LESS_THAN_OR_EQUAL) {
                            score = 1 - score;
                        }
                    }
                    scores.add(i, score);
                }
            }
            experiment.setRewardVariablesScores(scores);
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
                    return parseRewardVariableValue(experiment.getBestPolicy().getMetricDisplayValues().get(rewardVarIndex));
                }
                return Double.NEGATIVE_INFINITY;
            };
        }

        @Override
        public int compare(Experiment e1, Experiment e2) {
            return Double.compare(keyExtractor.applyAsDouble(e1), keyExtractor.applyAsDouble(e2));
        }

    }

    static Double parseRewardVariableValue(String valueWithUncertainty) {
        return Double.parseDouble(removeThousandsSeparatorFromNumber(valueWithUncertainty).split("\u2800\u00B1\u2800")[0]);
    }

}