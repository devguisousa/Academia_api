package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
class MensalidadeServiceTest {
    @Mock MensalidadeRepository mensalidades;
    @Mock MatriculaRepository matriculas;
    @InjectMocks MensalidadeService service;
    private Mensalidade mensalidade(StatusMensalidade status, LocalDate vencimento) {
        Matricula matricula = new Matricula(); matricula.setId(2L);
        Mensalidade mensalidade = new Mensalidade(); mensalidade.setId(1L); mensalidade.setMatricula(matricula);
        mensalidade.setStatus(status); mensalidade.setDataVencimento(vencimento); return mensalidade;
    }
    @Test void cadastraComoPendente() {
        Matricula matricula = new Matricula(); matricula.setId(2L); matricula.setAtivo(true);
        MensalidadeRequest request = new MensalidadeRequest(); request.setMatriculaId(2L); request.setValor(new BigDecimal("100")); request.setDataVencimento(LocalDate.of(2030, 1, 10));
        when(matriculas.findById(2L)).thenReturn(Optional.of(matricula)); when(mensalidades.save(any())).thenAnswer(inv -> inv.getArgument(0));
        MensalidadeResponse response = service.cadastrar(request);
        assertEquals(StatusMensalidade.PENDENTE, response.getStatus()); assertEquals(request.getValor(), response.getValor()); assertEquals(request.getDataVencimento(), response.getDataVencimento()); assertEquals(2L, response.getMatriculaId());
    }
    @Test void rejeitaMatriculaDesativada() {
        Matricula matricula = new Matricula(); matricula.setAtivo(false);
        MensalidadeRequest request = new MensalidadeRequest(); request.setMatriculaId(2L);
        when(matriculas.findById(2L)).thenReturn(Optional.of(matricula));
        assertThrows(RuntimeException.class, () -> service.cadastrar(request)); verify(mensalidades, never()).save(any());
    }
    @ParameterizedTest @EnumSource(value = StatusMensalidade.class, names = {"PAGO", "CANCELADO"})
    void naoAlteraNemCancelaStatusFinal(StatusMensalidade status) {
        Mensalidade mensalidade = mensalidade(status, LocalDate.now()); when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        assertThrows(RuntimeException.class, () -> service.alterar(new MensalidadeRequest(), 1L));
        assertThrows(RuntimeException.class, () -> service.cancelar(1L)); verify(mensalidades, never()).save(any());
    }
    @Test void cancelaMensalidadePendente() {
        Mensalidade mensalidade = mensalidade(StatusMensalidade.PENDENTE, LocalDate.now());
        when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade)); when(mensalidades.save(mensalidade)).thenReturn(mensalidade);
        assertEquals(StatusMensalidade.CANCELADO, service.cancelar(1L).getStatus()); verify(mensalidades).save(mensalidade);
    }
    @Test void marcaPendenteVencidaComoAtrasada() {
        Mensalidade mensalidade = mensalidade(StatusMensalidade.PENDENTE, LocalDate.now().minusDays(2));
        when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        assertEquals(StatusMensalidade.ATRASADO, service.verificarAtraso(1L).getStatus()); verify(mensalidades).save(mensalidade);
    }
    @ParameterizedTest @EnumSource(value = StatusMensalidade.class, names = {"PAGO", "CANCELADO", "ATRASADO"})
    void verificarAtrasoPreservaOutrosStatus(StatusMensalidade status) {
        Mensalidade mensalidade = mensalidade(status, LocalDate.now().minusDays(2)); when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        assertEquals(status, service.verificarAtraso(1L).getStatus()); verify(mensalidades, never()).save(any());
    }
    @Test void vencimentoFuturoNaoFicaAtrasado() {
        when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade(StatusMensalidade.PENDENTE, LocalDate.now().plusDays(2))));
        assertEquals(StatusMensalidade.PENDENTE, service.verificarAtraso(1L).getStatus()); verify(mensalidades, never()).save(any());
    }

}
