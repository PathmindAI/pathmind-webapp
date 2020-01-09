package io.skymind.pathmind.db.utils;

import java.math.BigDecimal;

/**
 * This is required due to a limitation in JOOQ with it's handling of NaN: https://github.com/jOOQ/jOOQ/issues/5249
 * The issue is still outstanding even now in 2020 and it doesn't look like a resolution is coming in the near future
 * therefore we need to implement one of the suggested workarounds.
 */
public class JooqUtils
{
    // We actually have to return the value for NaN rather than the constant for JOOQ to work.
    public static BigDecimal getSafeBigDecimal(double value) {
        return Double.isNaN(value) ? null : new BigDecimal(value);
    }
}
