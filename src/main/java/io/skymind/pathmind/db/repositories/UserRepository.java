package io.skymind.pathmind.db.repositories;

import io.skymind.pathmind.data.PathmindUser;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static io.skymind.pathmind.data.db.Tables.PATHMIND_USER;
import static org.jooq.impl.DSL.lower;

@Repository
public class UserRepository
{
    @Autowired
    private DSLContext dslContext;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    public PathmindUser findByEmailIgnoreCase(String email) {
        return dslContext
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.EMAIL.equalIgnoreCase(email))
                .fetchOneInto(PathmindUser.class);
    }

    public PathmindUser findById(long id) {
        return dslContext
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.ID.eq(id))
                .fetchOneInto(PathmindUser.class);
    }

    public PathmindUser findByToken(String token) {
        return dslContext
                .selectFrom(PATHMIND_USER)
                .where(PATHMIND_USER.EMAIL_VERIFICATION_TOKEN.eq(UUID.fromString(token)))
                .fetchOneInto(PathmindUser.class);
    }

    /**
     * Change a user's password.
     * @param id the id of the user whose password will be changed
     * @param newPassword the new password
     * @return whether the password was updated or not
     */
    public boolean changePassword(long id, String newPassword) {
        return 1 == dslContext.update(PATHMIND_USER)
                .set(PATHMIND_USER.PASSWORD, passwordEncoder.encode(newPassword))
                .where(PATHMIND_USER.ID.eq(id))
                .execute();
    }

}
