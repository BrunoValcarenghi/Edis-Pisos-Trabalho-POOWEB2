package br.csi.appspring.app_spring_mvc.infra.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenServiceJWT {

    private final SecretKey chave;
    private final long duracaoMinutos;

    public TokenServiceJWT(
            @Value("${security.jwt.secret}") String segredo,
            @Value("${security.jwt.expiration-minutes:120}") long duracaoMinutos) {
        this.chave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(segredo));
        this.duracaoMinutos = duracaoMinutos;
    }

    public String gerar(String email) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(duracaoMinutos * 60)))
                .signWith(chave)
                .compact();
    }

    public String extrairEmail(String token) {
        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
