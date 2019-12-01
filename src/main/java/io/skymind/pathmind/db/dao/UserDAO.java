package io.skymind.pathmind.db.dao;

import io.skymind.pathmind.bus.EventBus;
import io.skymind.pathmind.bus.events.UserUpdateBusEvent;
import io.skymind.pathmind.data.PathmindUser;
import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO
{
	private final DSLContext ctx;
	protected PasswordEncoder passwordEncoder;

	public UserDAO(DSLContext ctx, PasswordEncoder passwordEncoder) {
		this.ctx = ctx;
		this.passwordEncoder = passwordEncoder;
	}

	public long insertUser(PathmindUser pathmindUser) {
		long pathmindUserId = UserSQL.insertUser(ctx, pathmindUser, passwordEncoder.encode(pathmindUser.getPassword()));
		pathmindUser.setId(pathmindUserId);
		return pathmindUserId;
	}

	public void update(PathmindUser pathmindUser) {
		UserSQL.update(ctx, pathmindUser);
		EventBus.post(new UserUpdateBusEvent(pathmindUser));
	}

	public void delete(long id) {
		UserSQL.delete(ctx, id);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToProject(long projectId) {
		return UserSQL.isUserAllowedAccessToProject(ctx, projectId);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToModel(long modelId) {
		return UserSQL.isUserAllowedAccessToModel(ctx, modelId);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToExperiment(long experimentId) {
		return UserSQL.isUserAllowedAccessToExperiment(ctx, experimentId);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToPolicy(long policyId) {
		return UserSQL.isUserAllowedAccessToPolicy(ctx, policyId);
	}

	public PathmindUser findByEmailIgnoreCase(String email) {
		return UserSQL.findByEmailIgnoreCase(ctx, email);
	}

	public PathmindUser findById(long id) {
		return UserSQL.findById(ctx, id);
	}

	public PathmindUser findByToken(String token) {
		return UserSQL.findByToken(ctx, token);
	}

	/**
	 * Change a user's password.
	 * @param id the id of the user whose password will be changed
	 * @param newPassword the new password
	 * @return whether the password was updated or not
	 */
	public boolean changePassword(long id, String newPassword) {
		return UserSQL.changePassword(ctx, id, passwordEncoder.encode(newPassword));
	}
}
