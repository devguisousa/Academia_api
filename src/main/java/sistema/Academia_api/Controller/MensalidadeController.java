package sistema.Academia_api.Controller;


import org.springframework.data.domain.Page;
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
