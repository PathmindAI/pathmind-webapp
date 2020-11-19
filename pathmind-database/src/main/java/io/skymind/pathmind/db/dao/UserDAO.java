package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.shared.data.PathmindUser;
import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    private final DSLContext ctx;
    protected PasswordEncoder passwordEncoder;

    public UserDAO(DSLContext ctx, PasswordEncoder passwordEncoder) {
        this.ctx = ctx;
        this.passwordEncoder = passwordEncoder;
    }

    public long insertUser(PathmindUser pathmindUser) {
        long pathmindUserId = UserRepository.insertUser(ctx, pathmindUser, passwordEncoder.encode(pathmindUser.getPassword()));
        pathmindUser.setId(pathmindUserId);
        return pathmindUserId;
    }

    public void update(PathmindUser pathmindUser) {
        UserRepository.update(ctx, pathmindUser);
    }

    public void delete(long id) {
        UserRepository.delete(ctx, id);
    }

    public PathmindUser findByEmailIgnoreCase(String email) {
        return UserRepository.findByEmailIgnoreCase(ctx, email);
    }

    public PathmindUser findById(long id) {
        return UserRepository.findById(ctx, id);
    }

    public PathmindUser findByToken(String token) {
        return UserRepository.findByToken(ctx, token);
    }

    public PathmindUser findByApiKey(String apiKey) {
        return UserRepository.findByApiKey(ctx, apiKey);
    }

    /**
     * Change a user's password.
     *
     * @param id          the id of the user whose password will be changed
     * @param newPassword the new password
     * @return whether the password was updated or not
     */
    public boolean changePassword(long id, String newPassword) {
        return UserRepository.changePassword(ctx, id, passwordEncoder.encode(newPassword));
    }
}
