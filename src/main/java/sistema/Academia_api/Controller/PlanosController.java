package sistema.Academia_api.Controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sistema.Academia_api.Planos.PlanoRequest;
import sistema.Academia_api.Planos.PlanoResponse;
import sistema.Academia_api.service.PlanoService;

import java.util.List;

@RestController
@RequestMapping("/planos")
@RequiredArgsConstructor
public class PlanosController {
    private final PlanoService planoService;

    @GetMapping
    public List<PlanoResponse> listar() {
        return planoService.listarPlanos();
    }

    @PostMapping
    public PlanoResponse cadastrar(@RequestBody @Valid PlanoRequest request) {
        return planoService.cadastrarPlano(request);
    }

    @GetMapping("/buscar/{id}")
    public PlanoResponse buscar(@PathVariable Long id) {
        return planoService.buscarPlanoPorId(id);
    }

    @PutMapping("/{id}")
    public PlanoResponse alterar(@PathVariable Long id, @RequestBody @Valid PlanoRequest request) {
        return planoService.alterarPlano(id, request);
    }

    @PutMapping("/{id}/desativar")
    public PlanoResponse desativar(@PathVariable Long id) {
        return planoService.desativarPlano(id);
    }

    @PutMapping("/{id}/ativar")
    public PlanoResponse ativar(@PathVariable Long id) {
        return planoService.ativarPlano(id);
    }
}
