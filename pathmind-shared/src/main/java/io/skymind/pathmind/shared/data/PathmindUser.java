package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.UserRole;
import io.skymind.pathmind.shared.data.user.DeepCloneableInterface;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PathmindUser implements DeepCloneableInterface<PathmindUser>
{
	private long id;
	private String email;
	private String password;
	private int accountType;
	private String firstname;
	private String lastname;
	private String address;
	private String city;
	private String state;
	private String country;
	private String zip;
	private LocalDateTime deleteAt;
	private LocalDateTime emailVerifiedAt;
	private UUID emailVerificationToken;
	private LocalDateTime passwordResetSendAt;
	private String stripeCustomerId;
	private String newEmailToVerify;

	private List<Project> projects;

    public String getName() {
		return firstname + " " + lastname;
	}

	public UserRole getAccountType() {
        return UserRole.getEnumFromId(this.accountType);
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    @Override
    public PathmindUser shallowClone() {
        return PathmindUser.builder()
                .id(id)
                .email(email)
                .password(password)
                .accountType(accountType)
                .firstname(firstname)
                .lastname(lastname)
                .address(address)
                .city(city)
                .state(state)
                .country(country)
                .zip(zip)
                .deleteAt(deleteAt)
                .emailVerifiedAt(emailVerifiedAt)
                .emailVerificationToken(emailVerificationToken)
                .passwordResetSendAt(passwordResetSendAt)
                .stripeCustomerId(stripeCustomerId)
                .newEmailToVerify(newEmailToVerify)
                .build();
    }
}

