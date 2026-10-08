package sistema.Academia_api.service;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.alunos.AlunoRequest;
import sistema.Academia_api.alunos.AlunoResponse;
import sistema.Academia_api.alunos.DashboardResponse;
import sistema.Academia_api.alunos.PerfilAlunoResponse;
import sistema.Academia_api.exception.AlunoException;
import sistema.Academia_api.exception.CpfJaCadastrado;
import sistema.Academia_api.exception.EmailJaCadastrado;
import sistema.Academia_api.frequencia.Frequencia;
import sistema.Academia_api.lancamentos.ResumoFinanceiroResponse;
import sistema.Academia_api.mensalidade.StatusMensalidade;
import sistema.Academia_api.repository.AlunoRepository;
import sistema.Academia_api.repository.FrenquenciaRepository;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.repository.MensalidadeRepository;

@Service
@RequiredArgsConstructor  
@Slf4j
public class AlunoService {

private final AlunoRepository alunoRepository; 
private final MatriculaRepository matriculaRepository;
private final FrenquenciaRepository frequenciaRepository;
private final MensalidadeRepository mensalidadeRepository;
private final LancamentoService lancamentoService;



    

private Aluno converterAluno (AlunoRequest request) {
    Aluno aluno = new Aluno();
    aluno.setNome(request.getNome());
    aluno.setEmail(request.getEmail());
    aluno.setPinCatraca(request.getPinCatraca());
    aluno.setDataNascimento(request.getDataNascimento());
    aluno.setCpf(request.getCpf());
    aluno.setTelefone(request.getTelefone());
    aluno.setSexo(request.getSexo());
    aluno.setEndereco(request.getEndereco());
    return aluno;
}

private  AlunoResponse converterAlunoResponse (Aluno aluno) {
    AlunoResponse response = new AlunoResponse();
    response.setId(aluno.getId());
    response.setNome(aluno.getNome());
    response.setEmail(aluno.getEmail());
    response.setDataNascimento(aluno.getDataNascimento());
    response.setCpf(aluno.getCpf());
    response.setTelefone(aluno.getTelefone());
    response.setSexo(aluno.getSexo());
    response.setEndereco(aluno.getEndereco());
    response.setAtivo(aluno.getAtivo());
    response.setFotoURL(aluno.getFotoURL());
    return response;
}

private Aluno salvarAluno(Aluno aluno) {
        return alunoRepository.save(aluno);
    }


public AlunoResponse cadastrarAluno(AlunoRequest request) {

Aluno aluno = converterAluno(request);

if (alunoRepository.existsByCpf(aluno.getCpf())) {
    log.warn("Operação cadastrarAluno bloqueada: CPF já cadastrado");
    throw new RuntimeException("CPF já cadastrado");
} 
if(alunoRepository.existsByEmail(aluno.getEmail())) {
    log.warn("Operação cadastrarAluno bloqueada: Email já cadastrado");
    throw new EmailJaCadastrado("Email já cadastrado");
}

Aluno alunoSalvo = salvarAluno(aluno);
    log.info("Operação cadastrarAluno concluída entidade=Aluno id={}", alunoSalvo.getId());
return converterAlunoResponse(alunoSalvo);

}

    public Page<AlunoResponse> buscar(String nome, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome, pageable);

        if (alunos.isEmpty()) {
            log.warn("Operação buscar bloqueada: Aluno não encontrado");
            throw new AlunoException("Aluno não encontrado");
        }

         return alunos.map(this::converterAlunoResponse);
    }

public void deletarAluno(Long id) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));

    alunoRepository.delete(aluno);
    log.info("Registro excluído entidade=Aluno id={}", aluno.getId());
}

public AlunoResponse atualizarAluno(Long id, AlunoRequest request) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));

    if (!aluno.getCpf().equals(request.getCpf())) {
        log.warn("Operação atualizarAluno bloqueada: CPF não pode ser alterado alunoId={}", id);
        throw new CpfJaCadastrado("CPF não pode ser alterado");
    }

    if (alunoRepository.existsByEmail(request.getEmail()) && !aluno.getEmail().equals(request.getEmail())) {
        log.warn("Operação atualizarAluno bloqueada: Email já cadastrado alunoId={}", id);
        throw new EmailJaCadastrado("Email já cadastrado");
    }

    aluno.setNome(request.getNome());
    aluno.setEmail(request.getEmail());
    aluno.setPinCatraca(request.getPinCatraca());
    aluno.setDataNascimento(request.getDataNascimento());
    aluno.setTelefone(request.getTelefone());
    aluno.setSexo(request.getSexo());
    aluno.setEndereco(request.getEndereco());
    Aluno alunoSalvo = salvarAluno(aluno);
    log.info("Operação atualizarAluno concluída entidade=Aluno id={}", alunoSalvo.getId());
    return converterAlunoResponse(alunoSalvo);

}     

public AlunoResponse ativarAluno(Long id) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));
    aluno.setAtivo(true);
    Aluno alunoSalvo = salvarAluno(aluno);
    log.info("Operação ativarAluno concluída entidade=Aluno id={}", alunoSalvo.getId());
    return converterAlunoResponse(alunoSalvo);
}

public AlunoResponse desativarAluno(Long id) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));
    aluno.setAtivo(false);
    Aluno alunoSalvo = salvarAluno(aluno);
    log.info("Operação desativarAluno concluída entidade=Aluno id={}", alunoSalvo.getId());
    return converterAlunoResponse(alunoSalvo);

}

public Page<AlunoResponse> listarAlunos(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Aluno> alunos = alunoRepository.findAll(pageable);

    return alunos.map(this::converterAlunoResponse);

}




public AlunoResponse salvarFotos(Long id, MultipartFile foto) throws IOException {

    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));
    Path pasta = Paths.get("uploads/alunos");

    Files.createDirectories(pasta);

    String fotosAlunos = id + "-" + foto.getOriginalFilename();
    Path caminhoFoto = pasta.resolve(fotosAlunos);
    Files.write(caminhoFoto, foto.getBytes());
    aluno.setFotoURL("/uploads/alunos/" + fotosAlunos);
    Aluno alunoSalvo = salvarAluno(aluno);
    log.info("Operação salvarFotos concluída entidade=Aluno id={}", alunoSalvo.getId());
    return converterAlunoResponse(alunoSalvo);
}

public PerfilAlunoResponse perfil(Long id) {

    Aluno aluno = alunoRepository.findById(id)
            .orElseThrow(() ->
                    new AlunoException("Aluno não encontrado"));

    PerfilAlunoResponse response = new PerfilAlunoResponse();

    response.setId(aluno.getId());
    response.setNome(aluno.getNome());
    response.setEmail(aluno.getEmail());
    response.setCpf(aluno.getCpf());
    response.setTelefone(aluno.getTelefone());
    response.setAtivo(aluno.getAtivo());
    response.setFotoURL(aluno.getFotoURL());

    Matricula matricula = matriculaRepository.findByAluno_IdAndAtivoTrue(id).orElse(null);

    if (matricula != null) {

        response.setPlano(matricula.getPlano().getNome());
        response.setInicioMatricula(matricula.getDataInicio());
        response.setFimMatricula(matricula.getDataFim());
        response.setMatriculaAtiva(matricula.getAtivo());


}

LocalDateTime hoje = LocalDate.now().atStartOfDay();
LocalDateTime fimHoje = LocalDate.now().atTime(23,59,59);

boolean compareceuHoje = frequenciaRepository.existsByAluno_IdAndDataEntradaBetween(id, hoje, fimHoje);

response.setCompareceuHoje(compareceuHoje);

Frequencia ultimaFrequencia = frequenciaRepository.findTopByAluno_IdOrderByDataEntradaDesc(id).orElse(null);

if (ultimaFrequencia != null) {
    response.setUltimaEntrada(ultimaFrequencia.getDataEntrada());

    long dias = ChronoUnit.DAYS.between(ultimaFrequencia.getDataEntrada().toLocalDate(), LocalDate.now());

    response.setDiasSemComparecer(dias);
    
}

return response;

}

public DashboardResponse dashboar(int ano, int mes) {
    DashboardResponse dashboar = new DashboardResponse();

    long alunosAtivos = alunoRepository.countByAtivoTrue();

    long mensalidadesPagas = mensalidadeRepository.countByStatus(StatusMensalidade.PAGO);
    long mensalidadesPendentes = mensalidadeRepository.countByStatus(StatusMensalidade.PENDENTE);
    long mensalidadesAtrasadas = mensalidadeRepository.countByStatus(StatusMensalidade.ATRASADO);

    ResumoFinanceiroResponse resumo = lancamentoService.resumoPorMes(ano, mes);

    

    BigDecimal totalReceita = resumo.getTotalReceitas();
    BigDecimal totalDespesas = resumo.getTotalDespesas();
    BigDecimal saldo = resumo.getSaldo();
    
    LocalDateTime inicioHoje = LocalDate.now().atStartOfDay();
    LocalDateTime fimHoje = LocalDate.now().atTime(LocalTime.MAX);

    long alunosComparaceram = frequenciaRepository.contarAlunosPorPeriodo(inicioHoje, fimHoje);

    dashboar.setAlunosAtivos(alunosAtivos);
    dashboar.setAlunosHoje(alunosComparaceram);
    dashboar.setMensalidadesPagas(mensalidadesPagas);
    dashboar.setMensalidadesPendentes(mensalidadesPendentes);
    dashboar.setMensalidadesAtrasadas(mensalidadesAtrasadas);
    dashboar.setTotalDespesas(totalDespesas);
    dashboar.setTotalReceitas(totalReceita);
    dashboar.setSaldo(saldo);

    return dashboar;
}

}