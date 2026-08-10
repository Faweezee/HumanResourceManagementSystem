package com.codewithfawaz.humanresourcemanagementsystem.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {
    SUPER_ADMIN_CREATE("super_admin:create"),
    SUPER_ADMIN_READ("super_admin:read"),
    SUPER_ADMIN_UPDATE("super_admin:update"),
    SUPER_ADMIN_DELETE("super_admin:delete"),

    HR_CREATE("hr:create"),
    HR_READ("hr:read"),
    HR_UPDATE("hr:update"),
    HR_DELETE("hr:delete"),

    MANAGER_CREATE("manager:create"),
    MANAGER_READ("manager:read"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_DELETE("manager:delete"),

    EMPLOYEE_CREATE("employee:create"),
    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_UPDATE("employee:update"),
    EMPLOYEE_DELETE("employee:delete");

    @Getter
    private final String permission;
}
