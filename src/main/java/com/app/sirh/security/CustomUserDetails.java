package com.app.sirh.security;

import java.util.Collection;
import java.util.Collections;

import com.app.sirh.model.Employe;
import com.app.sirh.model.Utilisateur;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetails implements UserDetails {

    private final Utilisateur utilisateur;
    private final Employe employe; // NULL si CANDIDAT

    public CustomUserDetails(Utilisateur utilisateur, Employe employe) {
        this.utilisateur = utilisateur;
        this.employe = employe;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().getCode()));
    }

    @Override
    public String getPassword() {
        return utilisateur.getMotPasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Employe getEmploye() {
        return employe;
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
        return utilisateur.isActif();
    }
}