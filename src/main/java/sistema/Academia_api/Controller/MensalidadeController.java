package sistema.Academia_api.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sistema.Academia_api.mensalidade.MensalidadeRequest;
import sistema.Academia_api.mensalidade.MensalidadeResponse;
import sistema.Academia_api.service.MensalidadeService;

@RestController 
@RequestMapping ("/mensalidades")
@RequiredArgsConstructor 
public class MensalidadeController {

private final MensalidadeService mensalidadeService;

@GetMapping("/buscar/{id}")

public MensalidadeResponse buscar(@PathVariable Long id) {
   return  mensalidadeService.buscar(id);

}

@PostMapping("/cadastrar")
public MensalidadeResponse cadastrar(@RequestBody @Valid MensalidadeRequest request) {
    return mensalidadeService.cadastrar(request);
}

@PutMapping ("/alterar/{id}")
public MensalidadeResponse alterar(@RequestBody @Valid MensalidadeRequest request, @PathVariable Long id ){
    return mensalidadeService.alterar(request, id);
}

@GetMapping("/listar")
public Page<MensalidadeResponse> listar (@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "15")int size) {
    return mensalidadeService.listar(page, size);
}    
@PutMapping ("/cancelar/{id}")
public MensalidadeResponse cancelar(@PathVariable Long id) {

    return mensalidadeService.cancelar(id);
}

}
