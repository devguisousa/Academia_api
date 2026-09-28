package sistema.Academia_api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.Matricula.MatriculaRequest;
import sistema.Academia_api.Matricula.MatriculaResponse;
import sistema.Academia_api.Planos.Plano;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.repository.AlunoRepository;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.repository.PlanoRepository;

@Service 
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;
    
    
    public MatriculaService(MatriculaRepository matriculaRepository, AlunoRepository alunoRepository,
            PlanoRepository planoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.alunoRepository = alunoRepository;
        this.planoRepository = planoRepository;
    }

    public Matricula salvarMatricula(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public Matricula converteMatricula(MatriculaRequest request) {
        
        Aluno aluno = alunoRepository.findById(request.getAlunoId())
        .orElseThrow(() -> new RuntimeException("aluno não encontrado"));

        Plano plano = planoRepository.findById(request.getPlanoId())
        .orElseThrow(()-> new RuntimeException("plano não encontrado"));

        Matricula matricula = new Matricula();

        matricula.setAluno(aluno);
        matricula.setPlano(plano);

        LocalDate dataInicio = LocalDate.now();

        matricula.setDataInicio(dataInicio);
        matricula.setDataFim(dataInicio.plusDays(plano.getDuracao()));
        matricula.setAtivo(true);

        return matricula;

    }


    public MatriculaResponse converterResponse(Matricula matricula) {

        MatriculaResponse response = new MatriculaResponse();

        response.setId(matricula.getId());

        response.setAlunoId(matricula.getAluno().getId());
        response.setPlanoId(matricula.getPlano().getId());

        response.setNomeAluno(matricula.getAluno().getNome());
        response.setNomePlano(matricula.getPlano().getNome());

        response.setDataInicio(matricula.getDataInicio());
        response.setDataFim(matricula.getDataFim());
        response.setAtivo(matricula.getAtivo());

        return response;
    }

    public MatriculaResponse cadastrarMatricula(MatriculaRequest request) {

    Matricula matricula = converteMatricula(request);

    Long alunoId = matricula.getAluno().getId();

     if(matriculaRepository.existsByAluno_IdAndAtivoTrue(alunoId)) {
        throw new RuntimeException("Aluno já possui outra matrícula ativa");
    }

    Matricula matriculaSalva = salvarMatricula(matricula);

    return converterResponse(matriculaSalva);
}

public List<MatriculaResponse> listar() {
    return matriculaRepository.findAll()
    .stream()
    .map(this::converterResponse)
    .collect(Collectors.toList());
}

public MatriculaResponse buscarMatricula(Long id){

    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Matricula não encontratada"));

    return converterResponse(matricula);


}

public MatriculaResponse ativarMatricula(Long id) {
    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Matricula não encontrada"));

    Long alunoId = matricula.getAluno().getId();

    if(matriculaRepository.existsByAluno_IdAndAtivoTrueAndIdNot(alunoId, id)) {
        throw new RuntimeException("Aluno já possui outra matrícula ativa");
    }
    matricula.setAtivo(true);

    Matricula matriculaSalva = salvarMatricula(matricula);

    return converterResponse(matriculaSalva);


}
 
public MatriculaResponse desativarMatricula(Long id) {
    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Matricula não encontrada"));
    matricula.setAtivo(false);

    Matricula matriculaSalva = salvarMatricula(matricula);

    return converterResponse(matriculaSalva);

}

public MatriculaResponse alterarMatricula(Long id, MatriculaRequest request) {
      
    Matricula matricula = matriculaRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Matricula não encontrada"));

    Aluno aluno = alunoRepository.findById(request.getAlunoId()).orElseThrow(()-> new RuntimeException("Aluno não encontrado"));

    Plano plano = planoRepository.findById(request.getPlanoId()).orElseThrow(() -> new RuntimeException("plano não encontrado"));

    matricula.setAluno(aluno);
    matricula.setPlano(plano);

    matricula.setDataFim(matricula.getDataInicio().plusMonths(plano.getDuracao()));

    Matricula matriculaSalva = salvarMatricula(matricula);

    return converterResponse(matriculaSalva);
    
}

}