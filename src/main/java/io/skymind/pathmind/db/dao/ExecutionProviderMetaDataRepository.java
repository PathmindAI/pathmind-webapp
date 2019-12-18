package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.skymind.pathmind.data.db.Tables.EXECUTION_PROVIDER_META_DATA;

// IMPORTANT -> The WHERE for ProviderClass is NOT included since it's always the same one providerClass at this time.
class ExecutionProviderMetaDataRepository
{
    protected static void put(DSLContext ctx, int providerClass, int type, String key, String value)
    {
        ctx.insertInto(EXECUTION_PROVIDER_META_DATA)
                .set(EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS, providerClass)
                .set(EXECUTION_PROVIDER_META_DATA.TYPE, type)
                .set(EXECUTION_PROVIDER_META_DATA.KEY, key)
                .set(EXECUTION_PROVIDER_META_DATA.VALUE, value)
                .execute();
    }

    protected static String get(DSLContext ctx, int type, String key)
    {
        Optional<String> optional = ctx.select(EXECUTION_PROVIDER_META_DATA.VALUE)
                .from(EXECUTION_PROVIDER_META_DATA)
                .where(EXECUTION_PROVIDER_META_DATA.TYPE.eq(type)
                        .and(EXECUTION_PROVIDER_META_DATA.KEY.eq(key)))
                .fetchOptional(EXECUTION_PROVIDER_META_DATA.VALUE, String.class);
        // We must use this pattern to be able to return null.
        return optional.isPresent() ? optional.get() : null;
    }

    protected static Map<String, String> get(DSLContext ctx, int type, List<String> keys)
    {
        return ctx.select(EXECUTION_PROVIDER_META_DATA.KEY, EXECUTION_PROVIDER_META_DATA.VALUE)
                .from(EXECUTION_PROVIDER_META_DATA)
                .where(EXECUTION_PROVIDER_META_DATA.TYPE.eq(type)
                        .and(EXECUTION_PROVIDER_META_DATA.KEY.in(keys)))
                .fetchMap(EXECUTION_PROVIDER_META_DATA.KEY, record -> {
                    // STEPH -> REFACTOR -> Can it ever be null? I don't believe so but it's an extra check for now.
                    if(record.get(EXECUTION_PROVIDER_META_DATA.VALUE) == null)
                        return null;
                    return record.get(EXECUTION_PROVIDER_META_DATA.VALUE);
                });
    }

    protected static void delete(DSLContext ctx, int type, String key) {
        ctx.deleteFrom(EXECUTION_PROVIDER_META_DATA)
                .where(EXECUTION_PROVIDER_META_DATA.TYPE.eq(type)
                        .and(EXECUTION_PROVIDER_META_DATA.KEY.eq(key)));
    }
}
