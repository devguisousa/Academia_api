package sistema.Academia_api.ServiceSegurity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sistema.Academia_api.user.User;

import static org.springframework.security.config.Elements.JWT;


@Service
public class JwtService {
    private final Algorithm algorithm;

    public JwtService(@Value ("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String gerarToken(User user) {

        Instant agora = Instant.now();

        return JWT.create()
        .withSubject(user.getEmail())
        .withClaim("role", user.getRole().name())
        .withIssuedAt(Date.from(agora))
        .withExpiresAt(Date.from(agora.plus(30,ChronoUnit.DAYS)))
        .sign(algorithm);
        
    }
}
