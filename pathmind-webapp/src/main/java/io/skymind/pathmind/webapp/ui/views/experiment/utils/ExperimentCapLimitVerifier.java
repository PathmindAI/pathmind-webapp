package io.skymind.pathmind.webapp.ui.views.experiment.utils;

import io.skymind.pathmind.db.dao.RunDAO;
import io.skymind.pathmind.shared.data.user.UserCaps;
import io.skymind.pathmind.shared.data.user.UserMetrics;
import io.skymind.pathmind.shared.security.SecurityUtils;
import io.skymind.pathmind.webapp.ui.plugins.SegmentIntegrator;
import io.skymind.pathmind.webapp.ui.utils.NotificationUtils;

import static io.skymind.pathmind.shared.data.user.UserMetrics.*;
import static io.skymind.pathmind.shared.data.user.UserMetrics.UserCapType.*;

public class ExperimentCapLimitVerifier {

    public static boolean isUserWithinCapLimits(RunDAO runDAO, UserCaps userCaps, SegmentIntegrator segmentIntegrator) {
        // Get the data user metrics. We need to get the runs rather then experiments, and specifically when they were run rather than created.
        UserMetrics userMetrics = runDAO.getRunUsageDataForUser(SecurityUtils.getUserId());
        // Criteria for our own notifications (the user's on screen notifications are slightly different).
        capLimitPathmindNotificationCheck(userMetrics, userCaps, segmentIntegrator);
        // Criteria for presenting the user with notifications.
        return capLimitUserNotificationCheck(userMetrics, userCaps);
    }

    private static boolean capLimitUserNotificationCheck(UserMetrics userMetrics, UserCaps userCaps) {
        if(userMetrics.getRunsCreatedToday() >= userCaps.getNewRunDailyLimit()) {
            showUserCapLimitNotification(Daily);
            return false;
        } else if(userMetrics.getRunsCreatedThisMonth() >= userCaps.getNewRunMonthlyLimit()) {
            showUserCapLimitNotification(Monthly);
            return false;
        } else {
            return true;
        }
    }

    private static void showUserCapLimitNotification(UserCapType userCapType) {
        NotificationUtils.showError("Maximum number of " + userCapType.name().toLowerCase() + " experiment runs has been reached.<br>" +
                "Please contact Pathmind support for assistance.");
    }

    /**
     * This is a notification for us to be able to reach out ot the customer before they reach the caps and as a result the checks are different. For example
     * even if it isWithinCap(userMetrics) we may want to send ourselves a notification (such as 75%, 90%, and of course when they reach the cap at 100%). These
     * checks have to be done for all cap types (which as of right now are daily and monthly).
     */
    private static void capLimitPathmindNotificationCheck(UserMetrics userMetrics, UserCaps userCaps, SegmentIntegrator segmentIntegrator) {
        capLimitPathmindNotificationCheckForType(Daily, userMetrics.getRunsCreatedToday(), userCaps, segmentIntegrator);
        capLimitPathmindNotificationCheckForType(Monthly, userMetrics.getRunsCreatedThisMonth(), userCaps, segmentIntegrator);
    }

    // IMPORTANT -> This will send ourselves notifications EVERY time the user creates a new experiment above newRunLowThreshold but the assumption
    // is that this should be extremely rare, and if it does happen then it's more cost effective for now than creating
    // a whole infrastructure for it. Worse case we could just push this to 90% rather than 75%. That being said if enough
    // people hit the newRunLowThreshold threshold then our caps are too low.
    // PS: I'm casting to integer since it's close enough for what we need here.
    private static void capLimitPathmindNotificationCheckForType(UserCapType userCapType, int runCount, UserCaps userCaps, SegmentIntegrator segmentIntegrator) {
        int percentage = (int) (runCount * 100f / getMaxAllowedRunCountForCapType(userCapType, userCaps));
        if (percentage >= userCaps.getNewRunNotificationThreshold())
            segmentIntegrator.userRunCapLimitReached(SecurityUtils.getUser(), userCapType, percentage);
    }

    private static int getMaxAllowedRunCountForCapType(UserCapType userCapType, UserCaps userCaps) {
        switch(userCapType) {
            case Daily:
                return userCaps.getNewRunDailyLimit();
            case Monthly:
                return userCaps.getNewRunMonthlyLimit();
            default:
                // Future proofing where if we miss adding a type it will fail.
                throw new RuntimeException("Invalid cap type.");
        }
    }
}
