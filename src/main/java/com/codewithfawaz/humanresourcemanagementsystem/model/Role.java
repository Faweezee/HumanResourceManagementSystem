package com.codewithfawaz.humanresourcemanagementsystem.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codewithfawaz.humanresourcemanagementsystem.model.Permissions.*;

@RequiredArgsConstructor
public enum Role {
    SUPER_ADMIN(Set.of(
            SUPER_ADMIN_CREATE,
            SUPER_ADMIN_READ,
            SUPER_ADMIN_UPDATE,
            SUPER_ADMIN_DELETE,
            HR_CREATE,
            HR_READ,
            HR_UPDATE,
            HR_DELETE,
            MANAGER_CREATE,
            MANAGER_READ,
            MANAGER_UPDATE,
            MANAGER_DELETE,
            EMPLOYEE_CREATE,
            EMPLOYEE_READ,
            EMPLOYEE_UPDATE,
            EMPLOYEE_DELETE
    )),
    HR(Set.of(
            HR_CREATE,
            HR_READ,
            HR_UPDATE,
            HR_DELETE
            )),
    MANAGER(Set.of(
            MANAGER_CREATE,
            MANAGER_READ,
            MANAGER_UPDATE,
            MANAGER_DELETE
            )),
    EMPLOYEE(Set.of(
            EMPLOYEE_CREATE,
            EMPLOYEE_READ,
            EMPLOYEE_UPDATE,
            EMPLOYEE_DELETE
             ));

    @Getter
    private final Set<Permissions> permissions;


    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(perm -> new SimpleGrantedAuthority(perm.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}
