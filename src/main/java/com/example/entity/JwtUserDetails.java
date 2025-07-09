package com.example.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
public class JwtUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    public JwtUserDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // 实现 UserDetails 接口方法
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return accountNonExpired; }
    @Override public boolean isAccountNonLocked() { return accountNonLocked; }
    @Override public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
    @Override public boolean isEnabled() { return enabled; }
}