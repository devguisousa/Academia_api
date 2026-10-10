package sistema.Academia_api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.Role;
import sistema.Academia_api.user.Usuario;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByRole(Role.ADMIN)) {
            log.info("Administrador já existente; criação inicial dispensada");
            return;
        }
        String nome = environment.getProperty("ADMIN_NOME");
        String email = environment.getProperty("ADMIN_EMAIL");
        String password = environment.getProperty("ADMIN_PASSWORD");
        if (!StringUtils.hasText(nome) || !StringUtils.hasText(email)
                || !StringUtils.hasText(password)) {
            log.warn("Administrador inicial não criado: configure ADMIN_NOME, ADMIN_EMAIL e ADMIN_PASSWORD");
            return;
        }
        if (usuarioRepository.existsByEmail(email)) {
            log.warn("Administrador inicial não criado: email já cadastrado");
            return;
        }
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(password));
        usuario.setRole(Role.ADMIN);
        usuarioRepository.save(usuario);
        log.info("Administrador inicial criado com sucesso");
    }
}
