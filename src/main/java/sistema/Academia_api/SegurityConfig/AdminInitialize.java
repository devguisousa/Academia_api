package sistema.Academia_api.SegurityConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.Role;
import sistema.Academia_api.user.Usuario;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class AdminInitialize implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value ("${ADMIN_NOME:}")
    private String adminNome;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        if (usuarioRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        if (adminNome.isBlank()
                || adminEmail.isBlank()
                || adminPassword.isBlank()) {

            log.warn("ADMIN inicial não criado: variáveis ADMIN_* não configuradas");
            return;
        }

        if (usuarioRepository.existsByEmail(adminEmail)) {
            log.warn("ADMIN inicial não criado: email já cadastrado");
            return;
        }

        Usuario admin = new Usuario();

        admin.setNome(adminNome);
        admin.setEmail(adminEmail);
        admin.setSenha(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        usuarioRepository.save(admin);

        log.info("Administrador inicial criado com sucesso");
    }
}


