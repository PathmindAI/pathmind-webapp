package io.skymind.pathmind.services.project;

import java.util.List;

import io.skymind.pathmind.db.utils.GridSortOrder;
import io.skymind.pathmind.shared.data.Experiment;
import io.skymind.pathmind.shared.data.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyServersGridService {

    public List<Experiment> getFilteredExperimentsForUser(long userId, boolean isArchived, int offset, int limit, List<GridSortOrder> sortOrders) {
        return null;//getFilteredExperimentsForUser(userId, isArchived, offset, limit, sortOrders);
    }

    public int countFilteredExperiments(long userId, boolean isArchived) {
        return 0;//countFilteredExperiments(userId, isArchived);
    }

    public int countExperiments(long userId) {
        return 0; //countExperiments(userId);
    }
}
