package sistema.Academia_api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.exception.AlunoException;
import sistema.Academia_api.exception.RegraNegocioException;
import sistema.Academia_api.frequencia.Frequencia;
import sistema.Academia_api.frequencia.FrequenciaRequest;
import sistema.Academia_api.frequencia.FrequenciaResponse;
import sistema.Academia_api.repository.AlunoRepository;
import sistema.Academia_api.repository.FrenquenciaRepository;

@Service 
@RequiredArgsConstructor 
public class FrequenciaService {

    private final FrenquenciaRepository frenquenciaRepository;
    private final AlunoRepository alunoRepository;

    private Frequencia converterFrequencia(FrequenciaRequest request) {
    
    Aluno aluno = alunoRepository.findById(request.getAlunoId())
    .orElseThrow(()-> new AlunoException("aluno não existe"));
    
    Frequencia frequencia = new Frequencia();

    frequencia.setAluno(aluno);
    frequencia.setDataEntrada(LocalDateTime.now());
        
        return frequencia;
} 

private FrequenciaResponse converterFrequenciaResponse(Frequencia frequencia) {

    FrequenciaResponse response = new FrequenciaResponse();

    response.setId(frequencia.getId());
    response.setAlunoId(frequencia.getAluno().getId());
    response.setDataEntrada(frequencia.getDataEntrada());

    return response;
}

private Frequencia salvarFrequencia(Frequencia frequencia){
return frenquenciaRepository.save(frequencia);
}


public FrequenciaResponse cadastrar(FrequenciaRequest request) {

    Frequencia frequencia = converterFrequencia(request);

    Frequencia frequenciaSalva = salvarFrequencia(frequencia);

    return converterFrequenciaResponse(frequenciaSalva);
}

public Page<FrequenciaResponse> listar(int page, int size) {
    
    Pageable pageable = PageRequest.of(page, size);

    Page<Frequencia> frequencia = frenquenciaRepository.findAll(pageable);

    return frequencia.map(this::converterFrequenciaResponse);

}

public Page<FrequenciaResponse> buscar(String nome, int page,int size) {

    Pageable pageable = PageRequest.of(page, size);

    Page<Frequencia> frequencias = frenquenciaRepository.findByAluno_NomeContainingIgnoreCase(nome, pageable);

    return frequencias.map(this::converterFrequenciaResponse);
}

public String compareceuHoje(Long alunoId) {

    alunoRepository.findById(alunoId).orElseThrow(()-> new AlunoException("Aluno não encontrado"));
    
    
    
    LocalDate hoje = LocalDate.now();

    LocalDateTime inicio = hoje.atStartOfDay();
    LocalDateTime fim = hoje.atTime(23,59,59);

   boolean veioHoje = frenquenciaRepository
   .existsByAluno_IdAndDataEntradaBetween(alunoId, inicio, fim);

   if(veioHoje) {
    return "veio hoje";
   }

   return "não veio hoje";

}

public long diasSemComparecer(Long alunoId) {

     alunoRepository.findById(alunoId).orElseThrow(()-> new AlunoException("Aluno não encontrado"));
    

    Frequencia ultimaFrequencia = frenquenciaRepository.findTopByAluno_IdOrderByDataEntradaDesc(alunoId)
    .orElseThrow(() -> new RegraNegocioException("Aluno ainda não possui registro de frequência"));

    LocalDate ultimaData = ultimaFrequencia.getDataEntrada().toLocalDate();

    return ChronoUnit.DAYS.between(ultimaData, LocalDate.now());

}
    
}
