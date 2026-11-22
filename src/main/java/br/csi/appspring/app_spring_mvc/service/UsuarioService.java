package br.csi.appspring.app_spring_mvc.service;

import br.csi.appspring.app_spring_mvc.dto.TokenResponseDTO;
import br.csi.appspring.app_spring_mvc.dto.UsuarioCadastroRequestDTO;
import br.csi.appspring.app_spring_mvc.dto.UsuarioLoginRequestDTO;
import br.csi.appspring.app_spring_mvc.infra.security.TokenServiceJWT;
import br.csi.appspring.app_spring_mvc.infra.security.UsuarioPrincipal;
import br.csi.appspring.app_spring_mvc.model.Usuario;
import br.csi.appspring.app_spring_mvc.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenServiceJWT tokenService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          TokenServiceJWT tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Transactional
    public TokenResponseDTO cadastrar(UsuarioCadastroRequestDTO request) {
        String email = request.email().trim().toLowerCase();

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Este email já está cadastrado.");
        }

        Usuario usuario = new Usuario(
                request.nome().trim(),
                email,
                passwordEncoder.encode(request.senha()));

        usuario = usuarioRepository.save(usuario);
        return criarResposta(usuario.getEmail(), usuario.getNome(), usuario.getId());
    }

    public TokenResponseDTO entrar(UsuarioLoginRequestDTO request) {
        try {
            UsernamePasswordAuthenticationToken credenciais =
                    new UsernamePasswordAuthenticationToken(
                            request.email().trim().toLowerCase(), request.senha());
            var autenticacao = authenticationManager.authenticate(credenciais);
            UsuarioPrincipal usuario = (UsuarioPrincipal) autenticacao.getPrincipal();

            return criarResposta(usuario.getUsername(), usuario.getNome(), usuario.getId());
        } catch (AuthenticationException erro) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email ou senha inválidos.");
        }
    }

    private TokenResponseDTO criarResposta(String email, String nome, int usuarioId) {
        String token = tokenService.gerar(email);
        return new TokenResponseDTO(token, "Bearer", nome, usuarioId);
    }
}
