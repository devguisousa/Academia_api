package sistema.Academia_api.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sistema.Academia_api.Matricula.MatriculaRequest;
import sistema.Academia_api.Matricula.MatriculaResponse;
import sistema.Academia_api.service.MatriculaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping("/matriculas") 
public class MatriculaController {
    
private final MatriculaService matriculaService;

public MatriculaController(MatriculaService service) {
    this.matriculaService = service;
}


@PostMapping

public MatriculaResponse cadastrar(@RequestBody @Valid MatriculaRequest request) {
    return matriculaService.cadastrarMatricula(request);
}

@GetMapping("/buscar/{id}")

public MatriculaResponse buscar(@PathVariable  Long id) {
    return matriculaService.buscarMatricula(id);
}


@GetMapping

public Page<MatriculaResponse> listar(@RequestParam(defaultValue = "0")int page,
                                     @RequestParam(defaultValue = "20") int size) {

    return matriculaService.listar(page, size);
}

@PutMapping("/{id}")

public MatriculaResponse alterar(@PathVariable Long id, @RequestBody @Valid MatriculaRequest request ) {
    return matriculaService.alterarMatricula(id, request);
}

@PutMapping("/{id}/ativar")

public MatriculaResponse ativar(@PathVariable Long id) {
    return matriculaService.ativarMatricula(id);
}

@PutMapping("/{id}/desativar")

public MatriculaResponse desativar(@PathVariable Long id) {
    return matriculaService.desativarMatricula(id);
}

}
 