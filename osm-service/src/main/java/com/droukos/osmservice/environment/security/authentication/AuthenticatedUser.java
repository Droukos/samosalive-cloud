package com.droukos.osmservice.environment.security.authentication;

import com.droukos.osmservice.environment.dto.RequesterAccessTokenData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements Authentication {

    private static final long serialVersionUID = 6861381095901879822L;
    private final transient RequesterAccessTokenData requesterAccessTokenData;
    private final List<SimpleGrantedAuthority> roles;
    private boolean authenticated = true;

    public AuthenticatedUser(RequesterAccessTokenData requesterAccessTokenData, List<SimpleGrantedAuthority> roles) {
        this.requesterAccessTokenData = requesterAccessTokenData;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public Object getCredentials() {
        return this.requesterAccessTokenData;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.requesterAccessTokenData;
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
        return this.requesterAccessTokenData.getUserId();
    }
}