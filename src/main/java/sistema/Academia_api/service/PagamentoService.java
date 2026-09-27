package sistema.Academia_api.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sistema.Academia_api.mensalidade.Mensalidade;
import sistema.Academia_api.pagamentos.Pagamento;
import sistema.Academia_api.pagamentos.PagamentoRequest;
import sistema.Academia_api.pagamentos.PagamentoResponse;
import sistema.Academia_api.repository.MensalidadeRepository;
import sistema.Academia_api.repository.PagamentoRepository;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final MensalidadeRepository mensalidadeRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, MensalidadeRepository mensalidadeRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.mensalidadeRepository = mensalidadeRepository;
    }

    public Pagamento converterPagamento(PagamentoRequest request) {

        Mensalidade mensalidade = mensalidadeRepository.findById(request.getMensalidadeId())
                .orElseThrow(() -> new RuntimeException("mensalidade não existe"));

        Pagamento p = new Pagamento();

        p.setMensalidade(mensalidade);

        p.setValor(request.getValor());

        p.setFormaDePagamento(request.getFormaDePagamento());

        p.setDataPagamento(LocalDate.now());

        return p;

    }

    public PagamentoResponse conveterResponse(Pagamento pagamento) {
        PagamentoResponse response = new PagamentoResponse();

        response.setId(pagamento.getId());
        response.setMensalidadeId(pagamento.getMensalidade().getId());
        response.setValor(pagamento.getValor());
        response.setFormaDePagamento(pagamento.getFormaDePagamento());
        response.setDataPagamento(pagamento.getDataPagamento());

        return response;

    }

    public Pagamento salvar(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public Page<PagamentoResponse> listar(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Pagamento> pagamentos = pagamentoRepository.findAll(pageable);

        return pagamentos.map(this::conveterResponse);

    }
}
