package sistema.Academia_api.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.mensalidade.Mensalidade;
import sistema.Academia_api.mensalidade.MensalidadeRequest;
import sistema.Academia_api.mensalidade.MensalidadeResponse;
import sistema.Academia_api.mensalidade.StatusMensalidade;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.repository.MensalidadeRepository;

@Service 
public class MensalidadeService {

    private final MensalidadeRepository mensalidadeRepository;
    private final MatriculaRepository matriculaRepository;

    public MensalidadeService(MensalidadeRepository mensalidadeRepository, MatriculaRepository matriculaRepository) {
        this.mensalidadeRepository = mensalidadeRepository;
        this.matriculaRepository = matriculaRepository;
    }
    public Mensalidade converterMensalidade(MensalidadeRequest request){
        Matricula matricula = matriculaRepository.findById(request.getMatriculaId()).orElseThrow(() -> new RuntimeException("mantricula não encontrada"));
    
        Mensalidade mensalidade = new Mensalidade();

        mensalidade.setMatricula(matricula);
        mensalidade.setValor(request.getValor());
        mensalidade.setDataVencimento(request.getDataVencimento());
        mensalidade.setStatus(StatusMensalidade.PENDENTE);

        return mensalidade;
    
    
    }

    public MensalidadeResponse converterResponse(Mensalidade mensalidade) {

      MensalidadeResponse response = new MensalidadeResponse();
      
        response.setId(mensalidade.getId());
        response.setMatriculaId(mensalidade.getMatricula().getId());
        response.setValor(mensalidade.getValor());
        response.setDataVencimento(mensalidade.getDataVencimento());
        response.setStatus(mensalidade.getStatus());

        return response;
    }

    public Mensalidade salvarMensalidade(Mensalidade mensalidade) {
        return mensalidadeRepository.save(mensalidade);
    }


    public MensalidadeResponse cadastrar(MensalidadeRequest request) {

        Mensalidade mensalidade = converterMensalidade(request);

        Mensalidade mensalidadeSalva = salvarMensalidade(mensalidade);

        return converterResponse(mensalidadeSalva);

     }

     public Page<MensalidadeResponse> listar(int page, int size) {
         Pageable pageable = PageRequest.of(page, size);

         Page<Mensalidade> mensalidades = mensalidadeRepository.findAll(pageable);

         return mensalidades.map(this::converterResponse);
     }

     public MensalidadeResponse alterar(MensalidadeRequest request, Long id) {
        
        Mensalidade mensalidade = mensalidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensalidade não encontrada"));

        Matricula matricula = matriculaRepository.findById(request.getMatriculaId()).orElseThrow(() -> new RuntimeException("mantricula não encontrada"));

       
        mensalidade.setMatricula(matricula);
        mensalidade.setValor(request.getValor());
        mensalidade.setDataVencimento(request.getDataVencimento());

        Mensalidade mensalidadeNova = salvarMensalidade(mensalidade);

        return converterResponse(mensalidadeNova);
     }


     public MensalidadeResponse buscar(Long id) {
         Mensalidade mensalidade = mensalidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensalidade não encontrada"));

         return converterResponse(mensalidade);
     }

     public MensalidadeResponse cancelar(Long id)  {
         Mensalidade mensalidade = mensalidadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Mensalidade não encontrada"));

         mensalidade.setStatus(StatusMensalidade.CANCELADO);

         Mensalidade mensalidadeCancelada = salvarMensalidade(mensalidade);

         return converterResponse(mensalidadeCancelada);
     }

}
