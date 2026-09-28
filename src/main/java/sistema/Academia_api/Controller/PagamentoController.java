package sistema.Academia_api.Controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sistema.Academia_api.pagamentos.PagamentoRequest;
import sistema.Academia_api.pagamentos.PagamentoResponse;
import sistema.Academia_api.service.PagamentoService;


@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/listar")
    public Page<PagamentoResponse> listar(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "20") int size) {
        return pagamentoService.listar(page, size);
}

    @PostMapping ("/cadastrar")
    public PagamentoResponse cadastrar(@RequestBody @Valid PagamentoRequest request) {
        return pagamentoService.cadastrar(request);
    }

    @PutMapping("/alterar/{id}")
    public PagamentoResponse alterar(@PathVariable Long id, @RequestBody @Valid PagamentoRequest request) {
        return  pagamentoService.alterar(id, request);
    }

    @GetMapping ("/buscar/{id}")
    public PagamentoResponse buscar(@PathVariable Long id) {
        return pagamentoService.buscar(id);
    }

    @DeleteMapping ("/deletar/{id}")
    public void deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
    }
 
}
