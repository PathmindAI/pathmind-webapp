package io.skymind.pathmind.db;

import io.skymind.pathmind.data.PathmindUser;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.data.db.public_.Tables.PATHMIND_USER;

@Repository
public class UserRepository
{
    @Autowired
    private DSLContext dslContext;

    public PathmindUser getUserByEmailAndPassword(String email, String password) {
//    	return null;
        return dslContext
            .selectFrom(PATHMIND_USER)
            .where(PATHMIND_USER.EMAIL.eq(email)
            .and(PATHMIND_USER.PASSWORD.eq(password)))
            .fetchOneInto(PathmindUser.class);
    }
}
