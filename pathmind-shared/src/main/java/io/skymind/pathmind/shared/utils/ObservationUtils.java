package io.skymind.pathmind.shared.utils;

import java.util.Collection;

import io.skymind.pathmind.shared.data.Observation;
import org.apache.commons.collections4.CollectionUtils;


public class ObservationUtils {

    public static boolean areObservationsEqual(Collection<Observation> observations1, Collection<Observation> observations2) {
        return CollectionUtils.isEqualCollection(observations1, observations2);
    }
}
