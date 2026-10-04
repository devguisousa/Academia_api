package sistema.Academia_api.ServiceSegurity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sistema.Academia_api.user.Usuario;

@Service
public class JwtService {
    private final Algorithm algorithm;

    public JwtService(@Value ("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String gerarAcessToken(Usuario user) {

        Instant agora = Instant.now();

        return JWT.create()
        .withSubject(user.getEmail())
        .withClaim("role", user.getRole().name())
        .withClaim("tipo", "ACCESS")
        .withIssuedAt(Date.from(agora))
        .withExpiresAt(Date.from(agora.plus(30,ChronoUnit.MINUTES)))
        .sign(algorithm);
        
    }

    public String gerarRefreshToken(Usuario user) {
        Instant agora = Instant.now();

        return  JWT.create()
        .withSubject(user.getEmail())
        .withClaim("tipo", "REFRESH")
        .withIssuedAt(Date.from(agora))
        .withExpiresAt(Date.from(agora.plus(20,ChronoUnit.DAYS)))
        .sign(algorithm);

    }

    public String validarAcessToken(String token) {
        return JWT.require(algorithm)
        .withClaim("tipo", "ACCESS")
        .build()
        .verify(token)
        .getSubject();
    }

    public String validarRefreshToken(String token) {
        return JWT.require(algorithm)
        .withClaim("tipo", "REFRESH")
        .build()
        .verify(token)
        .getSubject();
    }
}
