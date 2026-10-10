package sistema.Academia_api.catraca;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.Planos.Plano;
import sistema.Academia_api.repository.*;
import sistema.Academia_api.service.FrequenciaService;
import sistema.Academia_api.exception.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeitorServiceTest {
    @Mock AlunoRepository alunos;
    @Mock MatriculaRepository matriculas;
    @Mock CatracaService catraca;
    @Mock FrequenciaService frequencia;
    @InjectMocks LeitorService leitor;
    IdentificacaoLeitorRequest request = new IdentificacaoLeitorRequest("123", TipoIdentificacao.PIN);

    Aluno aluno() {
        Aluno aluno = new Aluno(); aluno.setId(1L);
        when(alunos.findByPinCatraca("123")).thenReturn(List.of(aluno));
        return aluno;
    }
    Matricula matricula() {
        Matricula m = new Matricula(); m.setPlano(new Plano());
        m.setDataInicio(LocalDate.now().minusDays(1)); m.setDataFim(LocalDate.now());
        when(matriculas.findByAluno_IdAndAtivoTrue(1L)).thenReturn(Optional.of(m));
        return m;
    }
    @Test void liberaAlunoAutorizadoSemRegistrarPresenca() {
        aluno(); matricula();
        assertEquals(1L, leitor.identificar(request));
        verify(catraca).liberarEntrada(); verifyNoInteractions(frequencia);
    }
    @Test void negaAlunoInativo() {
        aluno().setAtivo(false);
        assertThrows(RegraNegocioException.class, () -> leitor.identificar(request));
        verifyNoInteractions(catraca, matriculas, frequencia);
    }
    @Test void negaMatriculaVencida() {
        aluno(); matricula().setDataFim(LocalDate.now().minusDays(1));
        assertThrows(RegraNegocioException.class, () -> leitor.identificar(request));
        verifyNoInteractions(catraca, frequencia);
    }
    @Test void negaSemMatricula() {
        aluno();
        assertThrows(RegraNegocioException.class, () -> leitor.identificar(request));
        verifyNoInteractions(catraca);
    }
    @Test void negaPinAmbiguo() {
        when(alunos.findByPinCatraca("123")).thenReturn(List.of(new Aluno(), new Aluno()));
        assertThrows(RegraNegocioException.class, () -> leitor.identificar(request));
        verifyNoInteractions(catraca, matriculas);
    }
    @Test void negaAlunoInexistente() {
        assertThrows(RecursoNaoEncontradoException.class, () -> leitor.identificar(request));
        verifyNoInteractions(catraca);
    }
    @Test void negaTipoNaoMapeado() {
        assertThrows(RegraNegocioException.class, () -> leitor.identificar(
                new IdentificacaoLeitorRequest("123", TipoIdentificacao.CARTAO)));
        verifyNoInteractions(alunos, catraca);
    }
    @Test void registraApenasPassagemConfirmada() {
        leitor.registrarPassagemConfirmada(1L);
        verify(frequencia).cadastrar(argThat(r -> r.getAlunoId().equals(1L)));
        verifyNoInteractions(catraca);
    }
}
