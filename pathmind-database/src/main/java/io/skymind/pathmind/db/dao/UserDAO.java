package io.skymind.pathmind.db.dao;

import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import io.skymind.pathmind.shared.data.PathmindUser;

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

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToProject(long projectId) {
		return UserRepository.isUserAllowedAccessToProject(ctx, projectId);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToModel(long modelId) {
		return UserRepository.isUserAllowedAccessToModel(ctx, modelId);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToExperiment(long experimentId) {
		return UserRepository.isUserAllowedAccessToExperiment(ctx, experimentId);
	}

	// TODO -> This cannot tell us if a user has access to an item because they item could be just none-existant. But for
	// now I'm using these method names so that we understand what needs to be done eventually.
	public boolean isUserAllowedAccessToPolicy(long policyId) {
		return UserRepository.isUserAllowedAccessToPolicy(ctx, policyId);
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

	/**
	 * Change a user's password.
	 * @param id the id of the user whose password will be changed
	 * @param newPassword the new password
	 * @return whether the password was updated or not
	 */
	public boolean changePassword(long id, String newPassword) {
		return UserRepository.changePassword(ctx, id, passwordEncoder.encode(newPassword));
	}
}
