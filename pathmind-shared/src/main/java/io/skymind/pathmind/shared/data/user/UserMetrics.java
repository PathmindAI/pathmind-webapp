package io.skymind.pathmind.shared.data.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Not stored in the database at this time.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMetrics {

    public enum UserCapType {
        Daily,
        Monthly
    }

    public static int MAX_EXPERIMENTS_ALLOWED_PER_DAY = 100;
    public static int MAX_EXPERIMENTS_ALLOWED_PER_MONTH = 1000;

    private int experimentsCreatedToday;
    private int experimentsCreatedThisMonth;
}
