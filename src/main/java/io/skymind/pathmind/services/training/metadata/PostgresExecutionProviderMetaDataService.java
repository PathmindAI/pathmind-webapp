package io.skymind.pathmind.services.training.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.data.db.tables.ExecutionProviderMetaData;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PostgresExecutionProviderMetaDataService implements ExecutionProviderMetaDataService {
    private final DSLContext ctx;
    private final ExecutionProviderMetaData tbl;
    private final ObjectMapper mapper;

    public PostgresExecutionProviderMetaDataService(DSLContext ctx, ObjectMapper mapper){
        this.ctx = ctx;
        this.mapper = mapper;

        tbl = Tables.EXECUTION_PROVIDER_META_DATA;
    }

    @Override
    public void put(Class<?> providerClazz, String key, Object value) {
        try {
            ctx.insertInto(tbl)
                    .set(tbl.PROVIDER_CLASS, providerClazz.getCanonicalName())
                    .set(tbl.KEY, key)
                    .set(tbl.VALUE, mapper.writeValueAsString(value))
                    .execute();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(Class<?> providerClazz, String key, Class<T> type) {
        final String value = ctx.selectFrom(tbl)
                .where(tbl.PROVIDER_CLASS.eq(providerClazz.getCanonicalName()).and(tbl.KEY.eq(key)))
                .fetchOne().get(tbl.KEY, String.class);

        try {
            return mapper.readValue(value, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Class<?> providerClazz, String key) {
        ctx.deleteFrom(tbl)
                .where(tbl.PROVIDER_CLASS.eq(providerClazz.getCanonicalName()).and(tbl.KEY.eq(key)));

    }
}
