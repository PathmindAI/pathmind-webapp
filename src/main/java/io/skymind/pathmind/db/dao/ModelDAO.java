package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.db.repositories.ModelRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.data.db.Tables.MODEL;

@Repository
public class ModelDAO extends ModelRepository
{

    private final DSLContext ctx;

    public ModelDAO(DSLContext ctx) {
        this.ctx = ctx;
    }

    public byte[] getModelFile(long id) {
        return ctx.select(MODEL.FILE).from(MODEL).where(MODEL.ID.eq(id)).fetchOne(MODEL.FILE);
    }
}
