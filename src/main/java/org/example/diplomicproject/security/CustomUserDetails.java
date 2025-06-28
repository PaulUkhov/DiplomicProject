package org.example.diplomicproject.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final String login;
    private final String password;
    private final List<SimpleGrantedAuthority> collect;

    public CustomUserDetails(String login, String password, List<SimpleGrantedAuthority> collect) {
        this.login = login;
        this.password = password;
        this.collect = collect;
    }

    //Взаимодействие user с авторизацией
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.collect;

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
