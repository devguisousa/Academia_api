package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
class LancamentoServiceTest {
    @Mock LancamentoRepository lancamentos;
    @InjectMocks LancamentoService service;
    private Lancamento lancamento(TipoLancamento tipo, String valor) {
        Lancamento lancamento = new Lancamento(); lancamento.setTipo(tipo); lancamento.setValor(new BigDecimal(valor)); return lancamento;
    }
    @Test void somaReceitasDespesasESaldo() {
        LocalDate inicio = LocalDate.of(2026, 1, 1), fim = LocalDate.of(2026, 1, 31);
        when(lancamentos.findByDataMensalBetween(inicio, fim)).thenReturn(List.of(lancamento(TipoLancamento.RECEITA, "100.00"), lancamento(TipoLancamento.RECEITA, "50.00"), lancamento(TipoLancamento.DESPESA, "40.00")));
        ResumoFinanceiroResponse response = service.resumoPorPeriodo(inicio, fim);
        assertEquals(new BigDecimal("150.00"), response.getTotalReceitas()); assertEquals(new BigDecimal("40.00"), response.getTotalDespesas()); assertEquals(new BigDecimal("110.00"), response.getSaldo());
    }
    @Test void mesBissextoUsaDia29() {
        LocalDate inicio = LocalDate.of(2024, 2, 1), fim = LocalDate.of(2024, 2, 29);
        when(lancamentos.findByDataMensalBetween(inicio, fim)).thenReturn(List.of());
        ResumoFinanceiroResponse response = service.resumoPorMes(2024, 2);
        assertEquals(BigDecimal.ZERO, response.getSaldo()); assertEquals(BigDecimal.ZERO, response.getTotalReceitas()); assertEquals(BigDecimal.ZERO, response.getTotalDespesas());
        verify(lancamentos).findByDataMensalBetween(inicio, fim);
    }
    @Test void resumoAnualUsaAnoCompleto() {
        when(lancamentos.findByDataMensalBetween(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31))).thenReturn(List.of());
        service.resumoPorAno(2026); verify(lancamentos).findByDataMensalBetween(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));
    }
    @Test void excluiLancamentoExistente() {
        Lancamento lancamento = lancamento(TipoLancamento.DESPESA, "10"); when(lancamentos.findById(1L)).thenReturn(Optional.of(lancamento));
        service.deletar(1L); verify(lancamentos).delete(lancamento);
    }
    @Test void naoExcluiLancamentoInexistente() {
        assertThrows(RuntimeException.class, () -> service.deletar(1L)); verify(lancamentos, never()).delete(any(Lancamento.class));
    }

}
