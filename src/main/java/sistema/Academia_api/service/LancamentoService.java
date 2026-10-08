package sistema.Academia_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sistema.Academia_api.lancamentos.Lancamento;
import sistema.Academia_api.lancamentos.LancamentoRequest;
import sistema.Academia_api.lancamentos.LancamentoResponse;
import sistema.Academia_api.lancamentos.ResumoFinanceiroResponse;
import sistema.Academia_api.lancamentos.TipoLancamento;
import sistema.Academia_api.repository.LancamentoRepository;

@Service
@RequiredArgsConstructor 
@Slf4j
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
    log.info("Operação cadastrar concluída entidade=Lancamento id={}", lancamentoSalvo.getId());

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
    log.info("Operação alterar concluída entidade=Lancamento id={}", lancamentoSalvo.getId());
    return converterLancamentoResponse(lancamentoSalvo);
}


public void deletar(Long id){
     Lancamento lancamento = lancamentoRepository.findById(id).orElseThrow(()-> new RuntimeException("lançamento não encontrado"));
     
     lancamentoRepository.delete(lancamento);
    log.info("Registro excluído entidade=Lancamento id={}", lancamento.getId());
}

public ResumoFinanceiroResponse resumoPorPeriodo(LocalDate inicio, LocalDate fim) {
    
    List<Lancamento> lancamentos = lancamentoRepository.findByDataMensalBetween(inicio, fim);

    BigDecimal totalReceita = BigDecimal.ZERO;
    BigDecimal totalDespesa = BigDecimal.ZERO;

    for(Lancamento lancamento : lancamentos) {

        if(lancamento.getTipo() == TipoLancamento.RECEITA) {
            totalReceita = totalReceita.add(lancamento.getValor());
        }

        if(lancamento.getTipo() == TipoLancamento.DESPESA) {
            totalDespesa = totalDespesa.add(lancamento.getValor());
        }
    }

    BigDecimal saldo = totalReceita.subtract(totalDespesa);

    ResumoFinanceiroResponse response = new ResumoFinanceiroResponse();

    response.setTotalReceitas(totalReceita);
    response.setTotalDespesas(totalDespesa);
    response.setSaldo(saldo);

    return response;
}


public ResumoFinanceiroResponse resumoPorMes(int ano, int mes) {
    YearMonth anoMes = YearMonth.of(ano, mes);

    LocalDate inicio = anoMes.atDay(1);
    LocalDate fim = anoMes.atEndOfMonth();

    return resumoPorPeriodo(inicio, fim);
}

public ResumoFinanceiroResponse resumoPorAno (int ano) {

    LocalDate inicio = LocalDate.of(ano, 1,1);
    LocalDate fim = LocalDate.of(ano, 12, 31);

    return resumoPorPeriodo(inicio, fim);
}

public Page<LancamentoResponse> listarPorPeriodo(
        LocalDate inicio,
        LocalDate fim,
        int page,
        int size) {

    Pageable pageable = PageRequest.of(page, size);

    Page<Lancamento> lancamentos =
            lancamentoRepository.findByDataMensalBetween(
                    inicio,
                    fim,
                    pageable
            );

    return lancamentos.map(this::converterLancamentoResponse);
}
 
}
