package sistema.Academia_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import sistema.Academia_api.lancamentos.Lancamento;
import sistema.Academia_api.lancamentos.LancamentoRequest;
import sistema.Academia_api.lancamentos.LancamentoResponse;
import sistema.Academia_api.repository.LancamentoRepository;

@Service
@RequiredArgsConstructor 
public class LancamentoService {

private final LancamentoRepository lancamentoRepository;

private Lancamento converterLancamento (LancamentoRequest request) {
    Lancamento lancamento = new Lancamento();

    lancamento.setDescricao(request.getDescricao());
    lancamento.setValor(request.getValor());
    lancamento.setDataMensal(request.getDataMensal());
    lancamento.setTipo(request.getTipo());

    return lancamento;
}


private LancamentoResponse converterLancamentoResponse(Lancamento lancamento) {
    LancamentoResponse response = new LancamentoResponse();

    response.setId(lancamento.getId());
    response.setDescricao(lancamento.getDescricao());
    response.setValor(lancamento.getValor());
    response.setDataMensal(lancamento.getDataMensal());
    response.setTipo(lancamento.getTipo());

    return response;
}

private Lancamento salvarLancamento(Lancamento lancamento) {
    return lancamentoRepository.save(lancamento);
}
    
public LancamentoResponse cadastrar(LancamentoRequest request) {

    Lancamento lancamento = converterLancamento(request);

    Lancamento lancamentoSalvo = salvarLancamento(lancamento);

    return converterLancamentoResponse(lancamentoSalvo);
}


public Page<LancamentoResponse> listar(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    Page<Lancamento> lancamentos = lancamentoRepository.findAll(pageable);

    return lancamentos.map(this::converterLancamentoResponse);

}

public Page<LancamentoResponse> buscar(String descricao, int page,int size) {
     Pageable pageable = PageRequest.of(page, size);

     Page<Lancamento> lancamentos = lancamentoRepository.findByDescricaoContainingIgnoreCase(descricao, pageable);

     return  lancamentos.map(this::converterLancamentoResponse);


}

public LancamentoResponse alterar(Long id, LancamentoRequest request) {
    Lancamento lancamento = lancamentoRepository.findById(id).orElseThrow(()-> new RuntimeException("lançamento não encontrado"));

    lancamento.setDescricao(request.getDescricao());
    lancamento.setValor(request.getValor());
    lancamento.setDataMensal(request.getDataMensal());
    lancamento.setTipo(request.getTipo());

    Lancamento lancamentoSalvo = salvarLancamento(lancamento);
    return converterLancamentoResponse(lancamentoSalvo);
}


public void deletar(Long id){
     Lancamento lancamento = lancamentoRepository.findById(id).orElseThrow(()-> new RuntimeException("lançamento não encontrado"));
     
     lancamentoRepository.delete(lancamento);
}


    
}
