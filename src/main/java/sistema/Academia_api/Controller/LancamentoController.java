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
import lombok.RequiredArgsConstructor;
import sistema.Academia_api.lancamentos.LancamentoRequest;
import sistema.Academia_api.lancamentos.LancamentoResponse;
import sistema.Academia_api.service.LancamentoService;

@RestController 
@RequestMapping("/lancamentos")
@RequiredArgsConstructor 
public class LancamentoController {
    private final LancamentoService lancamentoService;

    @GetMapping 
    public Page<LancamentoResponse> listar(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "15")int size) {
        return lancamentoService.listar(page, size);
    }

    @GetMapping ("/{descricao}")
    public Page<LancamentoResponse> buscar(@PathVariable  String descricao, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "15")int size) {
        return lancamentoService.buscar(descricao, page, size);

    }
    @PostMapping 
    public LancamentoResponse cadastrar(@RequestBody  @Valid LancamentoRequest request) {
        return lancamentoService.cadastrar(request);
    }

    @PutMapping ("/{id}")
    public LancamentoResponse alterar(@PathVariable Long id, @RequestBody @Valid LancamentoRequest request) {
        return  lancamentoService.alterar(id, request);
    }

    @DeleteMapping ("/{id}")
    public void deletar(@PathVariable Long id) {
        lancamentoService.deletar(id);
    }


}
