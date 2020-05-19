package io.skymind.pathmind.db.utils;

import org.jooq.DSLContext;

public class DBUtils {

    /**
     * Raises an exception if a transaction is locked for 'timeInSeconds' seconds.
     *
     * This is useful to prevent deadlocks when multiple transactions are trying to change the same set of objects.
     *
     * @param transactionCtx
     * @param timeInSeconds
     */
    public static void setLockTimeout(DSLContext transactionCtx, int timeInSeconds) {
        transactionCtx.execute(String.format("SET LOCAL lock_timeout = '%ss'", timeInSeconds));
    }
}
