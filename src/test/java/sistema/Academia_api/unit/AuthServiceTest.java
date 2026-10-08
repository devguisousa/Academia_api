package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import sistema.Academia_api.auth.AuthService;
import sistema.Academia_api.ServiceSegurity.*;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.*;
import sistema.Academia_api.user.login.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UsuarioRepository usuarios;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwt;
    @InjectMocks AuthService service;
    private LoginRequest request() {
        LoginRequest request = new LoginRequest(); request.setEmail("ana@example.com"); request.setSenha("senha"); return request;
    }
    private Usuario usuario() {
        Usuario usuario = new Usuario(); usuario.setEmail("ana@example.com"); usuario.setSenha("hash"); return usuario;
    }
    @Test void loginEmiteTokenParaCredenciaisValidas() {
        Usuario usuario = usuario(); when(usuarios.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches("senha", "hash")).thenReturn(true); when(jwt.gerarAcessToken(usuario)).thenReturn("token");
        LoginResponse response = service.login(request());
        assertEquals("token", response.getAcessToken()); assertEquals(usuario.getEmail(), response.getEmail());
    }
    @Test void senhaInvalidaNaoEmiteToken() {
        when(usuarios.findByEmail("ana@example.com")).thenReturn(Optional.of(usuario()));
        assertEquals("Email ou senha inválidos", assertThrows(RuntimeException.class, () -> service.login(request())).getMessage()); verifyNoInteractions(jwt);
    }
    @Test void emailInexistenteNaoValidaSenhaNemEmiteToken() {
        assertThrows(RuntimeException.class, () -> service.login(request())); verifyNoInteractions(encoder, jwt);
    }
    @Test void refreshEmiteNovoTokenEPreservaRefreshToken() {
        Usuario usuario = usuario(); RefreshRequest request = new RefreshRequest(); request.setRefreshToken("refresh");
        when(jwt.validarRefreshToken("refresh")).thenReturn(usuario.getEmail()); when(usuarios.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(jwt.gerarAcessToken(usuario)).thenReturn("novo-token");
        LoginResponse response = service.refresh(request);
        assertEquals("novo-token", response.getAcessToken()); assertEquals("refresh", response.getRefreshToken()); assertEquals(usuario.getEmail(), response.getEmail()); verifyNoInteractions(encoder);
    }
    @Test void refreshInvalidoNaoConsultaUsuario() {
        RefreshRequest request = new RefreshRequest(); request.setRefreshToken("invalido");
        when(jwt.validarRefreshToken("invalido")).thenThrow(new IllegalArgumentException("Token inválido"));
        assertThrows(IllegalArgumentException.class, () -> service.refresh(request)); verifyNoInteractions(usuarios, encoder); verify(jwt, never()).gerarAcessToken(any());
    }
}
