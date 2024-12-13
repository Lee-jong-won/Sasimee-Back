package com.example.Sasimee_Back.dto;

import com.example.Sasimee_Back.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class SasimeePrincipal implements UserDetails {

    private final String useremail;
    private final String encrpytpassword;
    private final Collection<? extends GrantedAuthority> authorities; // 권한 정보

    public SasimeePrincipal(String useremail, String encrpytpassword, Collection<? extends GrantedAuthority> authorities){
        this.useremail = useremail;
        this.encrpytpassword = encrpytpassword;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return encrpytpassword;
    }

    @Override
    public String getUsername() {
        return useremail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
