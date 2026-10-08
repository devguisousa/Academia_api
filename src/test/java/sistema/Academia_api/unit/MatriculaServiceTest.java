package sistema.Academia_api.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
class MatriculaServiceTest {
    @Mock MatriculaRepository matriculas;
    @Mock AlunoRepository alunos;
    @Mock PlanoRepository planos;
    @InjectMocks MatriculaService service;
    Aluno aluno; Plano plano; MatriculaRequest request;
    @BeforeEach void preparar() {
        aluno = new Aluno(); aluno.setId(1L); aluno.setAtivo(true); aluno.setNome("Ana");
        plano = new Plano(); plano.setId(2L); plano.setAtivo(true); plano.setDuracao(30); plano.setNome("Mensal");
        request = new MatriculaRequest(); request.setAlunoId(1L); request.setPlanoId(2L);
    }
    private void simularBusca() {
        when(alunos.findById(1L)).thenReturn(Optional.of(aluno)); when(planos.findById(2L)).thenReturn(Optional.of(plano));
    }
    @Test void cadastraMatriculaComDuracaoDoPlano() {
        simularBusca(); when(matriculas.save(any())).thenAnswer(inv -> { Matricula m = inv.getArgument(0); m.setId(3L); return m; });
        MatriculaResponse response = service.cadastrarMatricula(request);
        assertEquals(3L, response.getId()); assertEquals(1L, response.getAlunoId()); assertEquals(2L, response.getPlanoId()); assertTrue(response.getAtivo());
        assertEquals(response.getDataInicio().plusDays(30), response.getDataFim()); assertTrue(response.getNumeroMatricula().matches("[0-9]{8}"));
    }
    @Test void rejeitaSegundaMatriculaAtiva() {
        simularBusca(); when(matriculas.existsByAluno_IdAndAtivoTrue(1L)).thenReturn(true);
        assertEquals("Aluno já possui outra matrícula ativa", assertThrows(RuntimeException.class, () -> service.cadastrarMatricula(request)).getMessage()); verify(matriculas, never()).save(any());
    }
    @Test void rejeitaAlunoDesativado() {
        simularBusca(); aluno.setAtivo(false);
        assertEquals("Aluno desativado", assertThrows(RuntimeException.class, () -> service.cadastrarMatricula(request)).getMessage()); verify(matriculas, never()).save(any());
    }
    @Test void rejeitaPlanoDesativado() {
        simularBusca(); plano.setAtivo(false);
        assertEquals("plano desativado", assertThrows(RuntimeException.class, () -> service.cadastrarMatricula(request)).getMessage()); verify(matriculas, never()).save(any());
    }
    @Test void rejeitaAlunoInexistente() {
        assertThrows(RuntimeException.class, () -> service.cadastrarMatricula(request)); verifyNoInteractions(planos, matriculas);
    }

}
