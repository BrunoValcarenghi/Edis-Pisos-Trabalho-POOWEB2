package br.csi.appspring.app_spring_mvc.infra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** Mantém o login das contas antigas e troca a senha por BCrypt após autenticar. */
public class PasswordEncoderComCompatibilidade implements PasswordEncoder {

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence senha) {
        return bcryptPasswordEncoder.encode(senha);
    }

    @Override
    public boolean matches(CharSequence senha, String senhaSalva) {
        if (senhaSalva == null) {
            return false;
        }

        if (ehBCrypt(senhaSalva)) {
            return bcryptPasswordEncoder.matches(senha, senhaSalva);
        }

        return MessageDigest.isEqual(
                senha.toString().getBytes(StandardCharsets.UTF_8),
                senhaSalva.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean upgradeEncoding(String senhaSalva) {
        return !ehBCrypt(senhaSalva);
    }

    private boolean ehBCrypt(String senha) {
        return senha.startsWith("$2a$")
                || senha.startsWith("$2b$")
                || senha.startsWith("$2y$");
    }
}
