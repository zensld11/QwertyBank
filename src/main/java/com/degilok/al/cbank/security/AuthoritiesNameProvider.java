package com.degilok.al.cbank.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthoritiesNameProvider {
    @Value("${security.authorities.roles.admin-role-name}")
    private String adminRole;

    @Value("${security.authorities.roles.user-role-name}")
    private String userRole;

    public String roleAdmin() {
        return adminRole;
    }

    public String roleUser() {
        return userRole;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public AuthoritiesNameProvider setAdminRole(String adminRole) {
        this.adminRole = adminRole;
        return this;
    }

    public String getUserRole() {
        return userRole;
    }

    public AuthoritiesNameProvider setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public List<String> getRolesName() {
        return List.of(roleAdmin(), roleUser());
    }
}