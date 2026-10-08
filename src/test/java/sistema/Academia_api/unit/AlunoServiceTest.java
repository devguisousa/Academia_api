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
class AlunoServiceTest {
    @Mock AlunoRepository alunos;
    @Mock MatriculaRepository matriculas;
    @Mock FrenquenciaRepository frequencias;
    @Mock MensalidadeRepository mensalidades;
    @Mock LancamentoService lancamentos;
    @InjectMocks AlunoService service;

    private AlunoRequest request() {
        AlunoRequest request = new AlunoRequest();
        request.setNome("Ana"); request.setCpf("12345678901"); request.setEmail("ana@example.com");
        return request;
    }
    @Test void cadastraAlunoEConverteResposta() {
        when(alunos.save(any(Aluno.class))).thenAnswer(inv -> {
            Aluno aluno = inv.getArgument(0); aluno.setId(1L); return aluno;
        });
        AlunoResponse response = service.cadastrarAluno(request());
        assertEquals(1L, response.getId()); assertEquals("Ana", response.getNome());
        assertEquals("12345678901", response.getCpf()); assertEquals("ana@example.com", response.getEmail());
        verify(alunos).save(argThat(a -> "Ana".equals(a.getNome()) && "12345678901".equals(a.getCpf())));
    }
    @Test void rejeitaCpfDuplicadoSemSalvar() {
        when(alunos.existsByCpf("12345678901")).thenReturn(true);
        assertEquals("CPF já cadastrado", assertThrows(RuntimeException.class, () -> service.cadastrarAluno(request())).getMessage());
        verify(alunos, never()).save(any());
    }
    @Test void rejeitaEmailDuplicadoSemSalvar() {
        when(alunos.existsByEmail("ana@example.com")).thenReturn(true);
        assertThrows(EmailJaCadastrado.class, () -> service.cadastrarAluno(request()));
        verify(alunos, never()).save(any());
    }
    @Test void impedeAlteracaoDoCpf() {
        Aluno aluno = new Aluno(); aluno.setCpf("outro-cpf");
        when(alunos.findById(1L)).thenReturn(Optional.of(aluno));
        assertThrows(CpfJaCadastrado.class, () -> service.atualizarAluno(1L, request()));
        verify(alunos, never()).save(any());
    }
    @Test void rejeitaExclusaoDeAlunoInexistente() {
        assertThrows(AlunoException.class, () -> service.deletarAluno(1L));
        verify(alunos, never()).delete(any(Aluno.class));
    }
    @Test void excluiAlunoEncontrado() {
        Aluno aluno = new Aluno(); when(alunos.findById(1L)).thenReturn(Optional.of(aluno));
        service.deletarAluno(1L); verify(alunos).delete(aluno);
    }
    @Test void buscaVaziaLancaExcecao() {
        when(alunos.findByNomeContainingIgnoreCase("Ana", PageRequest.of(0, 10))).thenReturn(Page.empty());
        assertThrows(AlunoException.class, () -> service.buscar("Ana", 0, 10));
    }
    @Test void listaAlunosComPaginacao() {
        Aluno aluno = new Aluno(); aluno.setId(1L); aluno.setNome("Ana");
        Pageable pageable = PageRequest.of(1, 5);
        when(alunos.findAll(pageable)).thenReturn(new PageImpl<>(List.of(aluno), pageable, 6));
        Page<AlunoResponse> response = service.listarAlunos(1, 5);
        assertEquals("Ana", response.getContent().getFirst().getNome()); assertEquals(6, response.getTotalElements());
        verify(alunos).findAll(pageable);
    }

}
