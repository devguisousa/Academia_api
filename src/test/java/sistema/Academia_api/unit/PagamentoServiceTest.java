package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
class PagamentoServiceTest {
    @Mock PagamentoRepository pagamentos;
    @Mock MensalidadeRepository mensalidades;
    @InjectMocks PagamentoService service;
    Mensalidade mensalidade;
    PagamentoRequest request;
    @BeforeEach void preparar() {
        mensalidade = new Mensalidade(); mensalidade.setId(1L); mensalidade.setValor(new BigDecimal("100.00")); mensalidade.setStatus(StatusMensalidade.PENDENTE);
        request = new PagamentoRequest(); request.setMensalidadeId(1L); request.setValor(new BigDecimal("100.0")); request.setFormaDePagamento(FormaDePagamento.PIX);
    }
    @Test void pagaMensalidadeComValorDeEscalaDiferente() {
        when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        when(pagamentos.save(any())).thenAnswer(inv -> { Pagamento pagamento = inv.getArgument(0); pagamento.setId(3L); return pagamento; });
        LocalDate inicio = LocalDate.now(); PagamentoResponse response = service.cadastrar(request);
        assertEquals(3L, response.getId()); assertEquals(1L, response.getMensalidadeId()); assertEquals(FormaDePagamento.PIX, response.getFormaDePagamento());
        assertFalse(response.getDataPagamento().isBefore(inicio)); assertFalse(response.getDataPagamento().isAfter(LocalDate.now()));
        assertEquals(StatusMensalidade.PAGO, mensalidade.getStatus()); verify(mensalidades).save(mensalidade);
    }
    @ParameterizedTest @EnumSource(value = StatusMensalidade.class, names = {"PAGO", "CANCELADO"})
    void rejeitaMensalidadePagaOuCancelada(StatusMensalidade status) {
        mensalidade.setStatus(status); when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        assertThrows(RuntimeException.class, () -> service.cadastrar(request));
        verify(pagamentos, never()).save(any()); verify(mensalidades, never()).save(any()); assertEquals(status, mensalidade.getStatus());
    }
    @Test void rejeitaValorDiferente() {
        request.setValor(new BigDecimal("99")); when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        assertEquals("Valor do pagamento deve ser igual ao valor da mensalidade", assertThrows(RuntimeException.class, () -> service.cadastrar(request)).getMessage());
        verify(pagamentos, never()).save(any()); verify(mensalidades, never()).save(any());
    }
    @Test void rejeitaMensalidadeInexistente() {
        assertThrows(RuntimeException.class, () -> service.cadastrar(request)); verifyNoInteractions(pagamentos);
    }
    @Test void falhaAoSalvarPagamentoNaoMarcaMensalidadeComoPaga() {
        when(mensalidades.findById(1L)).thenReturn(Optional.of(mensalidade));
        when(pagamentos.save(any())).thenThrow(new IllegalStateException("Falha ao salvar"));
        assertThrows(IllegalStateException.class, () -> service.cadastrar(request));
        assertEquals(StatusMensalidade.PENDENTE, mensalidade.getStatus()); verify(mensalidades, never()).save(any());
    }

}
