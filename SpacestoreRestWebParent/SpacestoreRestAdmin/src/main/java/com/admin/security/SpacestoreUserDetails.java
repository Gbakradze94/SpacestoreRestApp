package com.admin.security;

import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SpacestoreUserDetails implements UserDetails {

    //private static final long serialVersionUID = 1L;
    private UserDto userDto;

    public SpacestoreUserDetails(UserDto user){
        this.userDto = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = userDto.getRoles();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role:roles
             ) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getEmail();
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
        return userDto.isEnabled();
    }

}
