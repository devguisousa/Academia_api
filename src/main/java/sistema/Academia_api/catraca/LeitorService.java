package sistema.Academia_api.catraca;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.exception.RecursoNaoEncontradoException;
import sistema.Academia_api.exception.RegraNegocioException;
import sistema.Academia_api.frequencia.FrequenciaRequest;
import sistema.Academia_api.frequencia.FrequenciaResponse;
import sistema.Academia_api.repository.AlunoRepository;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.service.FrequenciaService;

@Service
@RequiredArgsConstructor
public class LeitorService {
    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository;
    private final CatracaService catracaService;
    private final FrequenciaService frequenciaService;

    public Long identificar(IdentificacaoLeitorRequest request) {
        if (request == null || request.tipo() == null || request.identificador() == null
                || request.identificador().isBlank()) {
            throw new RegraNegocioException("Identificação inválida");
        }
        // TODO: mapear cartão, biometria e senha aos IDs reais do equipamento.
        if (request.tipo() != TipoIdentificacao.PIN) {
            throw new RegraNegocioException("Tipo de identificação ainda não integrado");
        }
        var alunos = alunoRepository.findByPinCatraca(request.identificador());
        if (alunos.isEmpty()) throw new RecursoNaoEncontradoException("Aluno não encontrado");
        // O campo existente não é único; identificações ambíguas nunca liberam acesso.
        if (alunos.size() != 1) throw new RegraNegocioException("PIN associado a mais de um aluno");
        Aluno aluno = alunos.getFirst();
        if (!Boolean.TRUE.equals(aluno.getAtivo())) {
            throw new RegraNegocioException("Acesso negado: aluno desativado");
        }
        var matricula = matriculaRepository.findByAluno_IdAndAtivoTrue(aluno.getId())
                .orElseThrow(() -> new RegraNegocioException("Acesso negado: matrícula ativa não encontrada"));
        LocalDate hoje = LocalDate.now();
        if (matricula.getDataInicio() == null || matricula.getDataFim() == null
                || hoje.isBefore(matricula.getDataInicio()) || hoje.isAfter(matricula.getDataFim())) {
            throw new RegraNegocioException("Acesso negado: matrícula fora da vigência");
        }
        if (matricula.getPlano() == null || !Boolean.TRUE.equals(matricula.getPlano().getAtivo())) {
            throw new RegraNegocioException("Acesso negado: plano desativado");
        }
        // Não existe regra atual de bloqueio financeiro; não criar política paralela.
        catracaService.liberarEntrada();
        return aluno.getId();
    }

    // Ponto interno para o futuro callback autenticado e correlacionado ao giro real.
    // TODO: validar evento, correlacionar aluno e deduplicar antes de chamar este método.
    public FrequenciaResponse registrarPassagemConfirmada(Long alunoId) {
        FrequenciaRequest request = new FrequenciaRequest();
        request.setAlunoId(alunoId);
        return frequenciaService.cadastrar(request);
    }
}
