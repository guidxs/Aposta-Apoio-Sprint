package br.com.fiap.aposta_apoio.security;

import br.com.fiap.aposta_apoio.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * Utiliza a biblioteca Auth0 para criar tokens seguros com algoritmo HMAC256.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private Long expiration;

    /**
     * Gera um token JWT para o usuário autenticado.
     *
     * @param usuario Usuário autenticado
     * @return Token JWT assinado
     */
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("aposta-apoio-api")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withClaim("role", usuario.getRole())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    /**
     * Valida e extrai o subject (login) do token JWT.
     *
     * @param token Token JWT a ser validado
     * @return Login do usuário contido no token
     */
    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("aposta-apoio-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return "";
        }
    }

    /**
     * Gera a data de expiração do token baseada na configuração.
     *
     * @return Instant com a data/hora de expiração
     */
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusSeconds(expiration / 1000)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public Long getExpiration() {
        return expiration;
    }
}

