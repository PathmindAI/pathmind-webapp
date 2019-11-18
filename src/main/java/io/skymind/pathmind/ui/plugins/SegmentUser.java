package io.skymind.pathmind.ui.plugins;

import io.skymind.pathmind.security.PathmindUserDetails;

/**
 * Contains user information that is passed to client side and used in identify
 * calls in segment
 */
public class SegmentUser {
	private String id;
	private String name;
	private String email;

	public SegmentUser(PathmindUserDetails userDetails) {
		id = Long.toString(userDetails.getId());
		name = userDetails.getName();
		email = userDetails.getEmail();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !SegmentUser.class.isInstance(obj)) {
			return false;
		}
		SegmentUser user = SegmentUser.class.cast(obj);
		if (id == null || user.getId() == null) {
			return false;
		}
		return id.equals(user.getId());
	}
}
