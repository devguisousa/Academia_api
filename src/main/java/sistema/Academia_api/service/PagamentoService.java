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

    private Pagamento converterPagamento(PagamentoRequest request) {

        Mensalidade mensalidade = mensalidadeRepository.findById(request.getMensalidadeId())
                .orElseThrow(() -> new RuntimeException("mensalidade não existe"));

        Pagamento p = new Pagamento();

        p.setMensalidade(mensalidade);

        p.setValor(request.getValor());

        p.setFormaDePagamento(request.getFormaDePagamento());

        p.setDataPagamento(LocalDate.now());

        return p;

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

    public PagamentoResponse cadastrar(PagamentoRequest request) {
        Pagamento pagamento = converterPagamento(request);

        Pagamento pagamentoSalvo = salvarPagamento(pagamento);

        return converterPagamentoResponse(pagamentoSalvo);
    }

    public PagamentoResponse alterar(Long id, PagamentoRequest request) {

        Mensalidade mensalidade = mensalidadeRepository.findById(request.getMensalidadeId())
                .orElseThrow(() -> new RuntimeException("mensalidade não existe"));
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("pagamento não encontrado"));

        pagamento.setMensalidade(mensalidade);
        pagamento.setFormaDePagamento(request.getFormaDePagamento());
        pagamento.setValor(request.getValor());

        Pagamento pagamentoSalvo = salvarPagamento(pagamento);

        return converterPagamentoResponse(pagamentoSalvo);
    }

    public void deletar(Long id) {

        if(!pagamentoRepository.existsById(id)) {
            throw new RuntimeException("pagamento não existe");
        }

        pagamentoRepository.deleteById(id);

    }

}