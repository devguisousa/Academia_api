package sistema.Academia_api.service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sistema.Academia_api.exception.RecursoNaoEncontradoException;
import sistema.Academia_api.exception.RegraNegocioException;
import sistema.Academia_api.mensalidade.Mensalidade;
import sistema.Academia_api.mensalidade.StatusMensalidade;
import sistema.Academia_api.pagamentos.Pagamento;
import sistema.Academia_api.pagamentos.PagamentoRequest;
import sistema.Academia_api.pagamentos.PagamentoResponse;
import sistema.Academia_api.repository.MensalidadeRepository;
import sistema.Academia_api.repository.PagamentoRepository;

@Service
@Slf4j 
@RequiredArgsConstructor 
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final MensalidadeRepository mensalidadeRepository;


    private Pagamento converterPagamento(PagamentoRequest request) {

        Mensalidade mensalidade = mensalidadeRepository.findById(request.getMensalidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("mensalidade não existe"));

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
                .orElseThrow(() -> new RecursoNaoEncontradoException("pagamento não encontrado"));

        return converterPagamentoResponse(pagamento);

    }

    @Transactional 
    public PagamentoResponse cadastrar(PagamentoRequest request) {
        
        Pagamento pagamento = converterPagamento(request);

        Mensalidade mensalidade = pagamento.getMensalidade();

        if (mensalidade.getStatus() == StatusMensalidade.CANCELADO) {

             log.warn(
            "Tentativa de pagar mensalidade cancelada id={}",
            mensalidade.getId()
    );
        throw new RegraNegocioException("Mensalidade está cancelada");
        }

        if (mensalidade.getStatus() == StatusMensalidade.PAGO) {

             log.warn(
            "Tentativa de pagar mensalidade já paga id={}",
            mensalidade.getId()
    );

        throw new RegraNegocioException("Mensalidade já está paga");
        }
        
        if (pagamento.getValor().compareTo(mensalidade.getValor()) != 0)  {

            
    log.warn(
            "Valor de pagamento inválido mensalidadeId={}",
            mensalidade.getId()
    );
            throw new RegraNegocioException("Valor do pagamento deve ser igual ao valor da mensalidade");
        }
        
            Pagamento pagamentoSalvo = salvarPagamento(pagamento);
            
            mensalidade.setStatus(StatusMensalidade.PAGO);
            mensalidadeRepository.save(mensalidade);

            log.info(
        "Pagamento registrado id={} mensalidadeId={}",
        pagamentoSalvo.getId(),
        mensalidade.getId());
        
        
        return converterPagamentoResponse(pagamentoSalvo);
    }

}