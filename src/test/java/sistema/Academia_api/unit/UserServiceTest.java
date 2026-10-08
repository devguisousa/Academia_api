package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import sistema.Academia_api.ServiceSegurity.*;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.*;
import sistema.Academia_api.user.login.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UsuarioRepository usuarios;
    @Mock PasswordEncoder encoder;
    @InjectMocks UserService service;
    private UsuarioRequest request() {
        UsuarioRequest request = new UsuarioRequest(); request.setNome("Ana"); request.setEmail("ana@example.com"); request.setSenha("senha"); request.setRole(Role.RECEPCAO); return request;
    }
    @Test void cadastraUsuarioComSenhaCodificada() {
        when(encoder.encode("senha")).thenReturn("hash");
        when(usuarios.save(any())).thenAnswer(inv -> { Usuario usuario = inv.getArgument(0); usuario.setId(1L); return usuario; });
        UsuarioResponse response = service.cadastrar(request());
        assertEquals(1L, response.getId()); assertEquals("Ana", response.getNome()); assertEquals("ana@example.com", response.getEmail()); assertEquals(Role.RECEPCAO, response.getRole());
        verify(usuarios).save(argThat(usuario -> "hash".equals(usuario.getSenha()) && Role.RECEPCAO == usuario.getRole()));
    }
    @Test void emailDuplicadoNaoCodificaNemSalva() {
        when(usuarios.existsByEmail("ana@example.com")).thenReturn(true);
        assertEquals("email ja cadastrado", assertThrows(RuntimeException.class, () -> service.cadastrar(request())).getMessage());
        verifyNoInteractions(encoder); verify(usuarios, never()).save(any());
    }
}
