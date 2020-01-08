package io.skymind.pathmind.db.utils;

/**
 * This is required due to a limitation in JOOQ with it's handling of NaN: https://github.com/jOOQ/jOOQ/issues/5249
 * The issue is still outstanding even now in 2020 and it doesn't look like a resolution is coming in the near future
 * therefore we need to implement one of the suggested workarounds.
 */
public class JooqUtils
{
    // We actually have to return the value for NaN rather than the constant for JOOQ to work.
    public static double getSafeDouble(double value) {
        return Double.NaN == value ? Double.longBitsToDouble(0x7ff8000000000000L) : value;
    }
}
