package io.skymind.pathmind.shared.constants;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static io.skymind.pathmind.shared.constants.ViewPermission.BASIC_READ;
import static io.skymind.pathmind.shared.constants.ViewPermission.EXTENDED_READ;
import static io.skymind.pathmind.shared.constants.ViewPermission.SETTINGS_READ;

public enum UserRole {
    Trial(0, "Trial", Set.of(BASIC_READ)),
    Professional(1, "Professional", Set.of(BASIC_READ)),
    Enterprise(2, "Enterprise", Set.of(BASIC_READ)),
    Admin(3, "Admin", Set.of(BASIC_READ, SETTINGS_READ)),
    Master(4, "Master", Set.of(BASIC_READ, SETTINGS_READ)),
    Support(5, "Support", Set.of(BASIC_READ, EXTENDED_READ, SETTINGS_READ)),
    Partner(6, "Partner", Set.of(BASIC_READ));

    private int id;
    private String name;
    private final Set<ViewPermission> permissions;

    public static final EnumSet<UserRole> serviceRoles = EnumSet.of(Admin, Master, Support);

    public static boolean isInternalOrEnterpriseOrPartnerUser(UserRole role) {
        return isInternalUser(role) || role == Enterprise || role == Partner;
    }

    public static boolean isInternalUser(UserRole role) {
        return serviceRoles.contains(role);
    }

    public static boolean isPaidUser(UserRole role) {
        return role == Professional || role == Enterprise || role == Partner;
    }

    UserRole(int id, String name, Set<ViewPermission> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public String toString() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public static UserRole getEnumFromId(int id) {
        return Arrays.stream(values())
                .filter(userRole -> userRole.getId() == id)
                .findAny()
                .get();
    }

    public Set<ViewPermission> getPermissions() {
        return this.permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
