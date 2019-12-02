package io.skymind.pathmind.db.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.skymind.pathmind.data.db.Tables.EXECUTION_PROVIDER_META_DATA;

@Repository
class ExecutionProviderMetaDataSQL
{
    protected static void put(DSLContext ctx, ObjectMapper mapper, String providerClazz, String key, Object value)
    {
        try {
            ctx.insertInto(EXECUTION_PROVIDER_META_DATA)
                    .set(EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS, providerClazz)
                    .set(EXECUTION_PROVIDER_META_DATA.KEY, key)
                    // STEPH => REFACTOR -> We should remove this as a json value since it's always a String
                    .set(EXECUTION_PROVIDER_META_DATA.VALUE, JSON.valueOf(mapper.writeValueAsString(value)))
                    .execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // STEPH -> REFACTOR -> Convert from JSON to String in the database as we're only ever using String. All the logic will fail if it's anything but string.
    protected static String get(DSLContext ctx, String providerClazz, String key)
    {
        Optional<String> optional = ctx.select(EXECUTION_PROVIDER_META_DATA.VALUE)
                .from(EXECUTION_PROVIDER_META_DATA)
                .where(EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS.eq(providerClazz)
                        .and(EXECUTION_PROVIDER_META_DATA.KEY.eq(key)))
                // STEPH -> REFACTOR -> The JSON is just a string. Quick hack until I resolve issue ...
                .fetchOptional(EXECUTION_PROVIDER_META_DATA.VALUE, String.class);
        if(optional.isPresent())
            return optional.get().replaceAll("\"", "");
        return null;
    }

    // STEPH -> REFACTOR -> Convert from JSON to String in the database as we're only ever using String. All the logic will fail if it's anything but string.
    protected static Map<String, String> get(DSLContext ctx, String providerClazz, List<String> keys)
    {
        return ctx.select(EXECUTION_PROVIDER_META_DATA.KEY, EXECUTION_PROVIDER_META_DATA.VALUE)
                .from(EXECUTION_PROVIDER_META_DATA)
                .where(EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS.eq(providerClazz)
                        .and(EXECUTION_PROVIDER_META_DATA.KEY.in(keys)))
                // STEPH -> REFACTOR -> The JSON is just a string. Quick hack until I resolve issue ...
                .fetchMap(EXECUTION_PROVIDER_META_DATA.KEY, record -> {
                    // STEPH -> REFACTOR -> Can it ever be null? I don't believe so but it's an extra check for now.
                    if(record.get(EXECUTION_PROVIDER_META_DATA.VALUE) == null)
                        return null;
                    return record.get(EXECUTION_PROVIDER_META_DATA.VALUE).toString().replaceAll("\"", "");
                });
    }

    protected static void delete(DSLContext ctx, String providerClazz, String key) {
        ctx.deleteFrom(EXECUTION_PROVIDER_META_DATA)
                .where(EXECUTION_PROVIDER_META_DATA.PROVIDER_CLASS.eq(providerClazz)
                        .and(EXECUTION_PROVIDER_META_DATA.KEY.eq(key)));
    }
}
