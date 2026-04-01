package com.ieshermanosmachado.netbook.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ieshermanosmachado.netbook.model.Usuario;

// Clase Adapter: Convierte nuestro Usuario (la base de datos) en lo que Spring Security entiende (UserDetails)
public class SecurityUsuario implements UserDetails {

    private final Usuario usuario;

    public SecurityUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Le devolvemos a Spring el rol del usuario en su formato de Autoridad (ROLE_LECTOR, ROLE_AUTOR, etc.)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getPasswordHash();
    }

    // Usamos el email como "username" para el login
    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
    
    // Método extra por si necesitamos acceder a la entidad limpia en algún momento
    public Usuario getUsuario() {
        return usuario;
    }
}
