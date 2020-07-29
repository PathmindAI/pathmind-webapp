package io.skymind.pathmind.webapp.ui.mocks;

import java.time.LocalDateTime;
import java.time.Month;

public class Constants {
    private Constants() {
    }

    public static final LocalDateTime mockDate() {
        return LocalDateTime.of(2020, Month.APRIL, 30, 16, 47, 58);
    }
}