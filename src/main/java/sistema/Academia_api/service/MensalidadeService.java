package sistema.Academia_api.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.Matricula.Matricula;
import sistema.Academia_api.mensalidade.Mensalidade;
import sistema.Academia_api.mensalidade.MensalidadeRequest;
import sistema.Academia_api.mensalidade.MensalidadeResponse;
import sistema.Academia_api.mensalidade.StatusMensalidade;
import sistema.Academia_api.repository.MatriculaRepository;
import sistema.Academia_api.repository.MensalidadeRepository;

import java.time.LocalDate;
import java.util.List;


@Service 
@RequiredArgsConstructor 
public class MensalidadeService {

    private final MensalidadeRepository mensalidadeRepository;
    private final MatriculaRepository matriculaRepository;

    private  Mensalidade converterMensalidade(MensalidadeRequest request){
        Matricula matricula = matriculaRepository.findById(request.getMatriculaId()).orElseThrow(() -> new RuntimeException("matricula não encontrada"));
    
        Mensalidade mensalidade = new Mensalidade();

        mensalidade.setMatricula(matricula);
        mensalidade.setValor(request.getValor());
        mensalidade.setDataVencimento(request.getDataVencimento());
        mensalidade.setStatus(StatusMensalidade.PENDENTE);

        return mensalidade;
    
    
    }

    private  MensalidadeResponse converterResponse(Mensalidade mensalidade) {

      MensalidadeResponse response = new MensalidadeResponse();
      
        response.setId(mensalidade.getId());
        response.setMatriculaId(mensalidade.getMatricula().getId());
        response.setValor(mensalidade.getValor());
        response.setDataVencimento(mensalidade.getDataVencimento());
        response.setStatus(mensalidade.getStatus());

        return response;
    }

    private Mensalidade salvarMensalidade(Mensalidade mensalidade) {
        return mensalidadeRepository.save(mensalidade);
    }


    public MensalidadeResponse cadastrar(MensalidadeRequest request) {

        Mensalidade mensalidade = converterMensalidade(request);

        Matricula matricula = mensalidade.getMatricula();

        if(Boolean.FALSE.equals(matricula.getAtivo())) {
            throw new RuntimeException("matricula esta desativada");
        }

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

        if (mensalidade.getStatus() == StatusMensalidade.PAGO) {
            throw new RuntimeException("mensalidade já esta paga"); 
        }

        if (mensalidade.getStatus() == StatusMensalidade.CANCELADO) {
          throw new RuntimeException("mensalidade cancelada");  
        }

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

         if(mensalidade.getStatus() == StatusMensalidade.PAGO ) {
            throw new RuntimeException("mensalidade já esta paga");
         }

         if (mensalidade.getStatus() == StatusMensalidade.CANCELADO) {
            throw new RuntimeException("Mensalidade já está cancelada");
         }


         mensalidade.setStatus(StatusMensalidade.CANCELADO);


         Mensalidade mensalidadeCancelada = salvarMensalidade(mensalidade);

         return converterResponse(mensalidadeCancelada);
     }

     public MensalidadeResponse verificarAtraso(Long id) {
        Mensalidade mensalidade = mensalidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("mensalidade não encontrada")));

        if (mensalidade.getStatus() == StatusMensalidade.PENDENTE && mensalidade.getDataVencimento()
                .isBefore(LocalDate.now())) {

            mensalidade.setStatus(StatusMensalidade.ATRASADO);
            mensalidadeRepository.save(mensalidade);


        }
         return converterResponse(mensalidade);
     }

     @Scheduled(cron = "0 0 0 * * *")
    public void atualizarMensalidadeAtrasada() {
        List<Mensalidade> mensalidades = mensalidadeRepository
                .findByStatusAndDataVencimentoBefore(StatusMensalidade.PENDENTE, LocalDate.now());

        for (Mensalidade mensalidade : mensalidades) {
            if (mensalidade.getStatus() == StatusMensalidade.PENDENTE && mensalidade.getDataVencimento().isBefore(LocalDate.now())) {
                mensalidade.setStatus(StatusMensalidade.ATRASADO);
                mensalidadeRepository.save(mensalidade);
            }
        }
     }
}
