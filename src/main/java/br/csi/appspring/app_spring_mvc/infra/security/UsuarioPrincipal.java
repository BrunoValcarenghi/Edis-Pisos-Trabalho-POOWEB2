package br.csi.appspring.app_spring_mvc.infra.security;

import br.csi.appspring.app_spring_mvc.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {

    private final int id;
    private final String nome;
    private final String email;
    private final String senha;

    public UsuarioPrincipal(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public static UsuarioPrincipal from(Usuario usuario) {
        return new UsuarioPrincipal(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }
}
