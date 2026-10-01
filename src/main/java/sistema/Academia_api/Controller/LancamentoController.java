package sistema.Academia_api.Controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
import sistema.Academia_api.lancamentos.ResumoFinanceiroResponse;
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

    @GetMapping ("/buscar/{descricao}")
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

    @GetMapping("/resumo/periodo")
public ResumoFinanceiroResponse resumoPorPeriodo(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate inicio,

        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fim) {

    return lancamentoService.resumoPorPeriodo(inicio, fim);
}


@GetMapping("/resumo/mensal")
public ResumoFinanceiroResponse resumoPorMes(
        @RequestParam int ano,
        @RequestParam int mes) {

    return lancamentoService.resumoPorMes(ano, mes);
}


@GetMapping("/resumo/anual")
public ResumoFinanceiroResponse resumoPorAno(
        @RequestParam int ano) {

    return lancamentoService.resumoPorAno(ano);
}


@GetMapping("/periodo")
public Page<LancamentoResponse> listarPorPeriodo(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate inicio,

        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fim,

        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size) {

    return lancamentoService.listarPorPeriodo(
            inicio,
            fim,
            page,
            size
    );
}

}
