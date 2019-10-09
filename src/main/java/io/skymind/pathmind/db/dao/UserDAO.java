package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.data.PathmindUser;
import io.skymind.pathmind.db.repositories.UserRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static io.skymind.pathmind.data.db.Tables.PATHMIND_USER;

@Repository
public class UserDAO extends UserRepository
{

    private final DSLContext ctx;

    UserDAO(DSLContext ctx){
        this.ctx = ctx;
    }

    @Transactional
    public PathmindUser update(PathmindUser user) {
//        PATHMIND_USER.
//        ctx.update(PATHMIND_USER)
//                .set(PATHMIND_USER.EMAIL, isArchive)
//                .where(PATHMIND_USER.ID.eq(user.getId()))
//                .execute();
        return null;
    }
}
