package sistema.Academia_api.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.repository.PlanoRepository;
import sistema.Academia_api.Planos.Plano;
import sistema.Academia_api.Planos.PlanoRequest;
import sistema.Academia_api.Planos.PlanoResponse;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor  
public class PlanoService {

    private final PlanoRepository planoRepository;

    private  Plano converterPlano(PlanoRequest request) {
        Plano plano = new Plano();
        plano.setNome(request.getNome());
        plano.setValor(request.getValor());
        plano.setDuracao(request.getDuracao());
        plano.setAtivo(true);
        return plano;
    }

    private PlanoResponse converterPlanoResponse(Plano plano) {
        PlanoResponse response = new PlanoResponse();
        response.setId(plano.getId());
        response.setNome(plano.getNome());
        response.setValor(plano.getValor());
        response.setDuracao(plano.getDuracao());
        response.setAtivo(plano.getAtivo()); 
        return response;
    }

    private  Plano salvarPlano(Plano plano) {
        return planoRepository.save(plano);
    }

    public List<PlanoResponse> listarPlanos() {
        return planoRepository.findAll()
        .stream().map(this::converterPlanoResponse)
        .collect(Collectors.toList());
    }

    public PlanoResponse desativarPlano(Long id) {
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        plano.setAtivo(false);
        return converterPlanoResponse(planoRepository.save(plano));
    }

    public PlanoResponse ativarPlano(Long id) {
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        plano.setAtivo(true);
        return converterPlanoResponse(planoRepository.save(plano));
    }


    public PlanoResponse alterarPlano(Long id, PlanoRequest request) {
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        plano.setNome(request.getNome());
        plano.setValor(request.getValor());
        plano.setDuracao(request.getDuracao());
        return converterPlanoResponse(planoRepository.save(plano));

    }

    public PlanoResponse cadastrarPlano(PlanoRequest request) {
        Plano plano = converterPlano(request);
        return converterPlanoResponse(salvarPlano(plano));
    }
    public PlanoResponse buscarPlanoPorId(Long id) {
        Plano plano = planoRepository.findById(id).orElseThrow(() -> new RuntimeException("Plano não encontrado"));
        return converterPlanoResponse(plano);
    }
}
