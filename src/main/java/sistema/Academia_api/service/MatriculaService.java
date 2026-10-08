package sistema.Academia_api.service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.Matricula.MatriculaRequest;
import sistema.Academia_api.Matricula.MatriculaResponse;
import sistema.Academia_api.Planos.Plano;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.exception.AlunoException;
import sistema.Academia_api.exception.RecursoNaoEncontradoException;
import sistema.Academia_api.exception.RegraNegocioException;
import sistema.Academia_api.repository.AlunoRepository;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.repository.PlanoRepository;

@Service 
@RequiredArgsConstructor 
@Slf4j
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;






    private Matricula converteMatricula(MatriculaRequest request) {
        
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
        .orElseThrow(() -> new AlunoException("aluno não encontrado"));

        Plano plano = planoRepository.findById(request.getPlanoId())
        .orElseThrow(()-> new RecursoNaoEncontradoException("plano não encontrado"));

        Matricula matricula = new Matricula();

        matricula.setAluno(aluno);
        matricula.setPlano(plano);

        LocalDate dataInicio = LocalDate.now();

        matricula.setDataInicio(dataInicio);
        matricula.setDataFim(dataInicio.plusDays(plano.getDuracao()));
        matricula.setAtivo(true);

        return matricula;

    }


    private String gerarNumeroMatricula() {

        String numero;

        do {
         numero = String.valueOf(ThreadLocalRandom.current().nextInt(10_000_000, 1000_000_00));

        } while(matriculaRepository.existsByNumeroMatricula(numero));
            
        return numero;
    }


    private MatriculaResponse converterResponse(Matricula matricula) {

        MatriculaResponse response = new MatriculaResponse();

        response.setId(matricula.getId());

        response.setAlunoId(matricula.getAluno().getId());
        response.setPlanoId(matricula.getPlano().getId());

        response.setNomeAluno(matricula.getAluno().getNome());
        response.setNomePlano(matricula.getPlano().getNome());
        response.setNumeroMatricula(matricula.getNumeroMatricula());

        response.setDataInicio(matricula.getDataInicio());
        response.setDataFim(matricula.getDataFim());
        response.setAtivo(matricula.getAtivo());

        return response;
    }
    
    private  Matricula salvarMatricula(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public MatriculaResponse cadastrarMatricula(MatriculaRequest request) {

    Matricula matricula = converteMatricula(request);

    Long alunoId = matricula.getAluno().getId();

     if(matriculaRepository.existsByAluno_IdAndAtivoTrue(alunoId)) {
        log.warn("Operação cadastrarMatricula bloqueada: Aluno já possui outra matrícula ativa alunoId={} planoId={}", matricula.getAluno().getId(), matricula.getPlano().getId());
        throw new RegraNegocioException("Aluno já possui outra matrícula ativa");
    }

    if (Boolean.FALSE.equals(matricula.getAluno().getAtivo())) {
    log.warn("Operação cadastrarMatricula bloqueada: Aluno desativado alunoId={} planoId={}", matricula.getAluno().getId(), matricula.getPlano().getId());
    throw new RegraNegocioException("Aluno desativado");
}

    if(Boolean.FALSE.equals(matricula.getPlano().getAtivo())) {
        log.warn("Operação cadastrarMatricula bloqueada: plano desativado alunoId={} planoId={}", matricula.getAluno().getId(), matricula.getPlano().getId());
        throw new RegraNegocioException("plano desativado");
    }

    matricula.setNumeroMatricula(gerarNumeroMatricula());


    Matricula matriculaSalva = salvarMatricula(matricula);
    log.info("Operação cadastrarMatricula concluída entidade=Matricula id={}", matriculaSalva.getId());

    return converterResponse(matriculaSalva);
}

public Page<MatriculaResponse> listar(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Matricula> matriculas = matriculaRepository.findAll(pageable);
    return  matriculas.map(this::converterResponse);
}

public MatriculaResponse buscarMatricula(Long id){

    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RecursoNaoEncontradoException("Matricula não encontratada"));

    return converterResponse(matricula);


}

public MatriculaResponse ativarMatricula(Long id) {
    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RecursoNaoEncontradoException("Matricula não encontrada"));

    Long alunoId = matricula.getAluno().getId();

    if(matriculaRepository.existsByAluno_IdAndAtivoTrueAndIdNot(alunoId, id)) {
        log.warn("Operação ativarMatricula bloqueada: Aluno já possui outra matrícula ativa alunoId={} planoId={}", matricula.getAluno().getId(), matricula.getPlano().getId());
        throw new RegraNegocioException("Aluno já possui outra matrícula ativa");
    }

    if (Boolean.FALSE.equals(matricula.getAluno().getAtivo())) {
    log.warn("Operação ativarMatricula bloqueada: Aluno desativado alunoId={} planoId={}", matricula.getAluno().getId(), matricula.getPlano().getId());
    throw new RegraNegocioException("Aluno desativado");
    }

    if (Boolean.FALSE.equals(matricula.getPlano().getAtivo())) {
    log.warn("Operação ativarMatricula bloqueada: Plano desativado alunoId={} planoId={}", matricula.getAluno().getId(), matricula.getPlano().getId());
    throw new RegraNegocioException("Plano desativado");
    }
    
    matricula.setAtivo(true);

    Matricula matriculaSalva = salvarMatricula(matricula);
    log.info("Operação ativarMatricula concluída entidade=Matricula id={}", matriculaSalva.getId());

    return converterResponse(matriculaSalva);


}
 
public MatriculaResponse desativarMatricula(Long id) {
    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RecursoNaoEncontradoException("Matricula não encontrada"));
    matricula.setAtivo(false);

    Matricula matriculaSalva = salvarMatricula(matricula);
    log.info("Operação desativarMatricula concluída entidade=Matricula id={}", matriculaSalva.getId());

    return converterResponse(matriculaSalva);

}

public MatriculaResponse alterarMatricula(Long id, MatriculaRequest request) {
      
    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RecursoNaoEncontradoException("Matricula não encontrada"));

    Plano plano = planoRepository.findById(request.getPlanoId()).orElseThrow(() -> new RecursoNaoEncontradoException("plano não encontrado"));
   
   
    if (Boolean.FALSE.equals(plano.getAtivo())) {
        log.warn("Operação alterarMatricula bloqueada: Plano desativado matriculaId={} planoId={}", id, plano.getId());
        throw new RegraNegocioException("Plano desativado");
    }
    
    matricula.setPlano(plano);

    matricula.setDataFim(matricula.getDataInicio().plusDays(plano.getDuracao()));

    Matricula matriculaSalva = salvarMatricula(matricula);
    log.info("Operação alterarMatricula concluída entidade=Matricula id={}", matriculaSalva.getId());

    return converterResponse(matriculaSalva);
    
}

}