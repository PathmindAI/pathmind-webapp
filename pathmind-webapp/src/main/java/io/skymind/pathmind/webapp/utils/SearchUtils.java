package io.skymind.pathmind.webapp.utils;

import java.time.LocalDateTime;

import io.skymind.pathmind.shared.utils.DateAndTimeUtils;
import org.apache.commons.lang3.StringUtils;

public class SearchUtils {
    private SearchUtils() {
    }

    public static final boolean contains(LocalDateTime localDateTime, String searchValue) {
        return StringUtils.containsIgnoreCase(DateAndTimeUtils.STANDARD_DATE_ONLY_FOMATTER.format(localDateTime), searchValue);
    }

    public static final boolean contains(boolean value, String searchValue) {
        return StringUtils.containsIgnoreCase(Boolean.toString(value), searchValue);
    }

    public static final boolean contains(String value, String searchValue) {
        return StringUtils.containsIgnoreCase(value, searchValue);
    }

    public static final boolean contains(Object value, String searchValue) {
        return StringUtils.containsIgnoreCase(value.toString(), searchValue);
    }
}
