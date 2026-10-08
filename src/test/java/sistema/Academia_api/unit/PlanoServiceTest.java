package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import sistema.Academia_api.service.*;
import sistema.Academia_api.repository.*;
import sistema.Academia_api.alunos.*;
import sistema.Academia_api.Planos.*;
import sistema.Academia_api.Matricula.*;
import sistema.Academia_api.mensalidade.*;
import sistema.Academia_api.pagamentos.*;
import sistema.Academia_api.frequencia.*;
import sistema.Academia_api.lancamentos.*;
import sistema.Academia_api.exception.*;

@ExtendWith(MockitoExtension.class)
class PlanoServiceTest {
    @Mock PlanoRepository planos;
    @InjectMocks PlanoService service;
    @Test void cadastraPlanoAtivo() {
        PlanoRequest request = new PlanoRequest(); request.setNome("Mensal"); request.setValor(100.0); request.setDuracao(30);
        when(planos.save(any())).thenAnswer(inv -> { Plano plano = inv.getArgument(0); plano.setId(2L); return plano; });
        PlanoResponse response = service.cadastrarPlano(request);
        assertEquals(2L, response.getId()); assertTrue(response.getAtivo());
        assertEquals("Mensal", response.getNome()); assertEquals(100.0, response.getValor()); assertEquals(30, response.getDuracao());
    }
    @Test void desativaPlano() {
        Plano plano = new Plano(); plano.setAtivo(true);
        when(planos.findById(2L)).thenReturn(Optional.of(plano)); when(planos.save(plano)).thenReturn(plano);
        assertFalse(service.desativarPlano(2L).getAtivo()); verify(planos).save(plano);
    }
    @Test void ativaPlano() {
        Plano plano = new Plano(); plano.setAtivo(false);
        when(planos.findById(2L)).thenReturn(Optional.of(plano)); when(planos.save(plano)).thenReturn(plano);
        assertTrue(service.ativarPlano(2L).getAtivo()); verify(planos).save(plano);
    }
    @Test void planoInexistenteNaoPodeSerAlterado() {
        assertThrows(RuntimeException.class, () -> service.alterarPlano(2L, new PlanoRequest()));
        verify(planos, never()).save(any());
    }
    @Test void listaPlanosVazia() { when(planos.findAll()).thenReturn(List.of()); assertTrue(service.listarPlanos().isEmpty()); }

}
