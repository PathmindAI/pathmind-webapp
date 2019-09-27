package io.skymind.pathmind.services.training.db.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.skymind.pathmind.data.db.Tables;
import io.skymind.pathmind.data.db.tables.ExecutionProviderMetaData;
import io.skymind.pathmind.data.db.tables.records.ExecutionProviderMetaDataRecord;
import org.jooq.DSLContext;
import org.jooq.JSON;
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
            final ExecutionProviderMetaDataRecord record = tbl.newRecord();
            record.setProviderClass(providerClazz.getCanonicalName());
            record.setKey(key);
            record.setValue(JSON.valueOf(mapper.writeValueAsString(value)));
            record.attach(ctx.configuration());
            record.store();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(Class<?> providerClazz, String key, Class<T> type) {
        final ExecutionProviderMetaDataRecord record = ctx.selectFrom(tbl)
                .where(tbl.PROVIDER_CLASS.eq(providerClazz.getCanonicalName()).and(tbl.KEY.eq(key)))
                .fetchOne();
        if(record != null) {
            final String value = record.getValue().toString();
            try {
                return mapper.readValue(value, type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            return null;
        }
    }

    @Override
    public void delete(Class<?> providerClazz, String key) {
        ctx.deleteFrom(tbl)
                .where(tbl.PROVIDER_CLASS.eq(providerClazz.getCanonicalName()).and(tbl.KEY.eq(key)));

    }
}
