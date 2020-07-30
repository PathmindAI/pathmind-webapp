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
public class UserCaps {
    private int newRunDailyLimit;
    private int newRunMonthlyLimit;
    private int newRunNotificationThreshold;
}
