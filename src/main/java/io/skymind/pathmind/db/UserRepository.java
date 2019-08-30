package io.skymind.pathmind.db;

import io.skymind.pathmind.data.User;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.skymind.pathmind.data.db.tables.User.USER;

@Repository
public class UserRepository
{
    @Autowired
    private DSLContext dslContext;

    public User getUserByEmailAndPassword(String email, String password) {
        return dslContext
            .selectFrom(USER)
            .where(USER.EMAIL.eq(email)
            .and(USER.PASSWORD.eq(password)))
            .fetchOneInto(User.class);
    }
}
