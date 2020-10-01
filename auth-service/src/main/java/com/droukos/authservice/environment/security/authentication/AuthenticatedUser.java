package com.droukos.authservice.environment.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements Authentication {

	private static final long serialVersionUID = 6861381095901879822L;
	private final String userId;
    private boolean authenticated = true;
    private final List<SimpleGrantedAuthority> roles;

    public AuthenticatedUser(String userId, List<SimpleGrantedAuthority> roles){
        this.userId = userId;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public Object getCredentials() {
        return this.userId;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) {
        this.authenticated = b;
    }

    @Override
    public String getName() {
        return this.userId;
    }
}