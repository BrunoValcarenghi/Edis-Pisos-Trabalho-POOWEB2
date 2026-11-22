package br.csi.appspring.app_spring_mvc.infra.security;

import br.csi.appspring.app_spring_mvc.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService, UserDetailsPasswordService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email.trim())
                .map(UsuarioPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha inválidos."));
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String novaSenha) {
        var usuario = usuarioRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        usuario.setSenha(novaSenha);
        return UsuarioPrincipal.from(usuarioRepository.save(usuario));
    }
}
