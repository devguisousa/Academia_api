package sistema.Academia_api.ServiceSegurity;

import org.springframework.stereotype.Service;



@Service
public class JwtService {
    private final Algorithm algorithm;

    public JwtService(@org.springframework.beans.factory.annotation.Value ("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }
    
}
