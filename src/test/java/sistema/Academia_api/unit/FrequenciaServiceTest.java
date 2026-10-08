package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
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
class FrequenciaServiceTest {
    @Mock FrenquenciaRepository frequencias;
    @Mock AlunoRepository alunos;
    @InjectMocks FrequenciaService service;
    @Test void registraEntradaDoAluno() {
        Aluno aluno = new Aluno(); aluno.setId(1L); FrequenciaRequest request = new FrequenciaRequest(); request.setAlunoId(1L);
        when(alunos.findById(1L)).thenReturn(Optional.of(aluno)); when(frequencias.save(any())).thenAnswer(inv -> { Frequencia f = inv.getArgument(0); f.setId(2L); return f; });
        LocalDateTime inicio = LocalDateTime.now(); FrequenciaResponse response = service.cadastrar(request);
        assertEquals(2L, response.getId()); assertEquals(1L, response.getAlunoId()); assertFalse(response.getDataEntrada().isBefore(inicio)); assertFalse(response.getDataEntrada().isAfter(LocalDateTime.now()));
    }
    @Test void naoRegistraAlunoInexistente() {
        FrequenciaRequest request = new FrequenciaRequest(); request.setAlunoId(1L);
        assertThrows(AlunoException.class, () -> service.cadastrar(request)); verifyNoInteractions(frequencias);
    }
    @Test void informaPresencaHoje() {
        when(alunos.findById(1L)).thenReturn(Optional.of(new Aluno()));
        when(frequencias.existsByAluno_IdAndDataEntradaBetween(eq(1L), any(), any())).thenReturn(true);
        assertEquals("veio hoje", service.compareceuHoje(1L));
    }
    @Test void informaAusenciaHoje() {
        when(alunos.findById(1L)).thenReturn(Optional.of(new Aluno()));
        assertEquals("não veio hoje", service.compareceuHoje(1L));
    }
    @Test void alunoSemRegistrosLancaExcecao() {
        when(alunos.findById(1L)).thenReturn(Optional.of(new Aluno()));
        assertEquals("Aluno ainda não possui registro de frequência", assertThrows(RuntimeException.class, () -> service.diasSemComparecer(1L)).getMessage());
    }

}
