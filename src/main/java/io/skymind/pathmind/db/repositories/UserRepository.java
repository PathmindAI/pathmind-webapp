package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.PathmindUser;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.data.db.Tables.PATHMIND_USER;
import static org.jooq.impl.DSL.lower;

@Repository
public class UserRepository
{
    @Autowired
    private DSLContext dslContext;

    public PathmindUser findByEmailIgnoreCase(String email) {
        return dslContext
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.EMAIL.eq(lower(email)))
                .fetchOneInto(PathmindUser.class);
    }

    public PathmindUser findById(long id) {
        return dslContext
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.ID.eq(id))
                .fetchOneInto(PathmindUser.class);
    }

}
