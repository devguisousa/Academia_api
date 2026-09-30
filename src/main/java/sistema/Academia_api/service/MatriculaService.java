package sistema.Academia_api.service;

import java.time.LocalDate;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.Matricula.MatriculaRequest;
import sistema.Academia_api.Matricula.MatriculaResponse;
import sistema.Academia_api.Planos.Plano;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.repository.AlunoRepository;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.repository.PlanoRepository;

@Service 
@RequiredArgsConstructor 
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoRepository alunoRepository;
    private final PlanoRepository planoRepository;



    private Matricula converteMatricula(MatriculaRequest request) {
        
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


    private MatriculaResponse converterResponse(Matricula matricula) {

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
    
    private  Matricula salvarMatricula(Matricula matricula) {
        return matriculaRepository.save(matricula);
    }

    public MatriculaResponse cadastrarMatricula(MatriculaRequest request) {

    Matricula matricula = converteMatricula(request);

    Long alunoId = matricula.getAluno().getId();

     if(matriculaRepository.existsByAluno_IdAndAtivoTrue(alunoId)) {
        throw new RuntimeException("Aluno já possui outra matrícula ativa");
    }

    if (Boolean.FALSE.equals(matricula.getAluno().getAtivo())) {
    throw new RuntimeException("Aluno desativado");
}

    if(Boolean.FALSE.equals(matricula.getPlano().getAtivo())) {
        throw new RuntimeException("plano desativado");
    }


    Matricula matriculaSalva = salvarMatricula(matricula);

    return converterResponse(matriculaSalva);
}

public Page<MatriculaResponse> listar(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Matricula> matriculas = matriculaRepository.findAll(pageable);
    return  matriculas.map(this::converterResponse);
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

    if (Boolean.FALSE.equals(matricula.getAluno().getAtivo())) {
    throw new RuntimeException("Aluno desativado");
    }

    if (Boolean.FALSE.equals(matricula.getPlano().getAtivo())) {
    throw new RuntimeException("Plano desativado");
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

    Plano plano = planoRepository.findById(request.getPlanoId()).orElseThrow(() -> new RuntimeException("plano não encontrado"));
   
   
    if (Boolean.FALSE.equals(plano.getAtivo())) {
        throw new RuntimeException("Plano desativado");
    }
    
    matricula.setPlano(plano);

    matricula.setDataFim(matricula.getDataInicio().plusDays(plano.getDuracao()));

    Matricula matriculaSalva = salvarMatricula(matricula);

    return converterResponse(matriculaSalva);
    
}

}