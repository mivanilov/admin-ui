package org.mi.adminui.security.model;

import org.mi.adminui.data.feature.user.model.User;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails, AuthenticatedPrincipal {

    private final String name;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(final String name,
                         final String email,
                         final Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserPrincipal create(final User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);

        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole().getSecurityName()));
        }

        return new UserPrincipal(user.getName(), user.getEmail(), authorities);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
