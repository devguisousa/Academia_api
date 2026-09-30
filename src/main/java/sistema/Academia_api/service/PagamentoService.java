package sistema.Academia_api.service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.mensalidade.Mensalidade;
import sistema.Academia_api.mensalidade.StatusMensalidade;
import sistema.Academia_api.pagamentos.Pagamento;
import sistema.Academia_api.pagamentos.PagamentoRequest;
import sistema.Academia_api.pagamentos.PagamentoResponse;
import sistema.Academia_api.repository.MensalidadeRepository;
import sistema.Academia_api.repository.PagamentoRepository;

@Service
@RequiredArgsConstructor 
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final MensalidadeRepository mensalidadeRepository;


    private Pagamento converterPagamento(PagamentoRequest request) {

        Mensalidade mensalidade = mensalidadeRepository.findById(request.getMensalidadeId())
                .orElseThrow(() -> new RuntimeException("mensalidade não existe"));

        Pagamento pagamento = new Pagamento();

        pagamento.setMensalidade(mensalidade);

        pagamento.setValor(request.getValor());

        pagamento.setFormaDePagamento(request.getFormaDePagamento());

        pagamento.setDataPagamento(LocalDate.now());

        return pagamento;

    }

    private PagamentoResponse converterPagamentoResponse(Pagamento pagamento) {
        PagamentoResponse response = new PagamentoResponse();

        response.setId(pagamento.getId());
        response.setMensalidadeId(pagamento.getMensalidade().getId());
        response.setValor(pagamento.getValor());
        response.setFormaDePagamento(pagamento.getFormaDePagamento());
        response.setDataPagamento(pagamento.getDataPagamento());

        return response;

    }

    private Pagamento salvarPagamento(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public Page<PagamentoResponse> listar(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Pagamento> pagamentos = pagamentoRepository.findAll(pageable);

        return pagamentos.map(this::converterPagamentoResponse);

    }

    public PagamentoResponse buscar(Long id) {

        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("pagamento não encontrado"));

        return converterPagamentoResponse(pagamento);

    }

    @Transactional 
    public PagamentoResponse cadastrar(PagamentoRequest request) {
        
        Pagamento pagamento = converterPagamento(request);

        Mensalidade mensalidade = pagamento.getMensalidade();

        if (mensalidade.getStatus() == StatusMensalidade.CANCELADO) {
        throw new RuntimeException("Mensalidade está cancelada");
        }

        if (mensalidade.getStatus() == StatusMensalidade.PAGO) {
        throw new RuntimeException("Mensalidade já está paga");
        }
        
        if (pagamento.getValor().compareTo(mensalidade.getValor()) != 0)  {
            throw new RuntimeException("Valor do pagamento deve ser igual ao valor da mensalidade");
        }
        
            Pagamento pagamentoSalvo = salvarPagamento(pagamento);
            
            mensalidade.setStatus(StatusMensalidade.PAGO);
            mensalidadeRepository.save(mensalidade);
        
        
        return converterPagamentoResponse(pagamentoSalvo);
    }

}